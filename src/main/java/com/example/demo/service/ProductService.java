package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.UnprocessableEntityException;
import com.example.demo.model.*;
import com.example.demo.param.ProductRequestParameter;
import com.example.demo.repository.IProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final IProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductService(@Qualifier("mapProductRepository") IProductRepository productRepository,
                          UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ProductVO getProductVO(String id) {
        var productPO = getProductPO(id);
        var productVO = ProductVO.of(productPO);

        var user = userRepository.getOneById(productPO.getCreatorId());
        productVO.setCreatorName(user.getName());

        return productVO;
    }

    public ProductPO createProduct(ProductCreateRequest productReq) {
        var userPO = userRepository.getOneById(productReq.getCreatorId());
        if (userPO == null) {
            throw new UnprocessableEntityException("Product creator " + productReq.getCreatorId() + " doesn't exist.");
        }

        var productPO = ProductPO.of(productReq);
        productPO = productRepository.insert(productPO);

        return productPO;
    }

    public void updateProduct(String id, ProductUpdateRequest productReq) {
        var productPO = getProductPO(id);
        productPO.setName(productReq.getName());
        productPO.setPrice(productReq.getPrice());
        productRepository.update(productPO);
    }

    public void deleteProduct(String id) {
        var productPO = getProductPO(id);
        productRepository.deleteById(productPO.getId());
    }

    public List<ProductVO> getProductVOs(ProductRequestParameter param) {
        var productPOs = productRepository.getMany(param);
        var userIds = productPOs
                .stream()
                .map(ProductPO::getCreatorId)
                .toList();
        var userIdNameMap = userRepository.getManyByIds(userIds)
                .stream()
                .collect(Collectors.toMap(UserPO::getId, UserPO::getName));
        return productPOs
                .stream()
                .map(po -> {
                    var vo = ProductVO.of(po);
                    vo.setCreatorName(userIdNameMap.get(po.getCreatorId()));
                    return vo;
                })
                .toList();
    }

    private ProductPO getProductPO(String id) {
        var po = productRepository.getOneById(id);
        if (po == null) {
            throw new NotFoundException("Product " + id + " doesn't exist.");
        }

        return po;
    }
}
