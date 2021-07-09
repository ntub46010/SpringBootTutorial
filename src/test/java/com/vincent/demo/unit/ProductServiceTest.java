package com.vincent.demo.unit;

import com.vincent.demo.auth.UserIdentity;
import com.vincent.demo.converter.ProductConverter;
import com.vincent.demo.entity.product.Product;
import com.vincent.demo.entity.product.ProductRequest;
import com.vincent.demo.entity.product.ProductResponse;
import com.vincent.demo.repository.ProductRepository;
import com.vincent.demo.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Test
    public void testConvertProductToResponse() {
        Product product = new Product();
        product.setId("123");
        product.setName("Snack");
        product.setPrice(50);
        product.setCreator("abc");
        ProductResponse productResponse = ProductConverter.toProductResponse(product);

        Assert.assertEquals(product.getId(), productResponse.getId());
        Assert.assertEquals(product.getName(), productResponse.getName());
        Assert.assertEquals(product.getPrice(), productResponse.getPrice());
        Assert.assertEquals(product.getCreator(), productResponse.getCreatorId());
    }

    @Test
    public void testGetProduct() {
        String productId = "123";
        Product testProduct = new Product();
        testProduct.setId(productId);
        testProduct.setName("Snack");
        testProduct.setPrice(50);
        testProduct.setCreator("abc");

        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(testProduct));
        ProductService productService = new ProductService(productRepository, null);

        Product resultProduct = productService.getProduct(productId);
        Assert.assertEquals(testProduct.getId(), resultProduct.getId());
        Assert.assertEquals(testProduct.getName(), resultProduct.getName());
        Assert.assertEquals(testProduct.getPrice(), resultProduct.getPrice());
        Assert.assertEquals(testProduct.getCreator(), resultProduct.getCreator());
    }

    @Test
    public void testCreateProduct() {
        ProductRepository productRepository = mock(ProductRepository.class);
        UserIdentity userIdentity = mock(UserIdentity.class);

        String creatorId = "abc";
        when(userIdentity.getId())
                .thenReturn(creatorId);
        ProductService productService = new ProductService(productRepository, userIdentity);

        ProductRequest productReq = new ProductRequest();
        productReq.setName("Snack");
        productReq.setPrice(50);
        ProductResponse productRes = productService.createProduct(productReq);

        InOrder inOrder = inOrder(productRepository, userIdentity);
        inOrder.verify(userIdentity, times(1)).getId();
        inOrder.verify(productRepository, times(1)).insert(any(Product.class));

        Assert.assertEquals(productReq.getName(), productRes.getName());
        Assert.assertEquals(productReq.getPrice().intValue(), productRes.getPrice());
        Assert.assertEquals(creatorId, productRes.getCreatorId());
    }
}
