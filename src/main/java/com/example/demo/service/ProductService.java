package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.UnprocessableEntityException;
import com.example.demo.model.ProductPO;
import com.example.demo.model.ProductRequest;
import com.example.demo.model.ProductVO;
import com.example.demo.param.ProductRequestParameter;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

import java.util.List;

public class ProductService {
    // Should use @Autowired
    private static final ProductRepository productRepository = new ProductRepository();
    private static final UserRepository userRepository = new UserRepository();

    public ProductVO getProductVO(String id) {
        var productPO = getProductPO(id);
        var productVO = ProductVO.of(productPO);

        var user = userRepository.getOneById(productPO.getCreatorId());
        productVO.setCreatorName(user.getName());

        return productVO;
    }

    public ProductPO createProduct(ProductRequest productReq) {
        var userPO = userRepository.getOneById(productReq.getCreatorId());
        if (userPO == null) {
            throw new UnprocessableEntityException("Product creator " + productReq.getCreatorId() + " doesn't exist.");
        }

        var productPO = ProductPO.of(productReq);
        productPO = productRepository.insert(productPO);

        return productPO;
    }

    public void updateProduct(String id, ProductRequest productReq) {
        var productPO = getProductPO(id);
        productPO.setName(productReq.getName());
        productPO.setPrice(productReq.getPrice());
        productRepository.update(productPO);
    }

    public void deleteProduct(String id) {
        var productPO = getProductPO(id);
        productRepository.deleteById(productPO.getId());
    }

    public List<ProductVO> get(ProductRequestParameter param) {
        var products = productRepository.getMany(param);
        return products
                .stream()
                .map(ProductVO::of)
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
