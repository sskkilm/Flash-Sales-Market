package com.example.product.application;

import com.example.product.domain.Product;
import com.example.product.dto.*;
import com.example.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void 상품_목록을_조회한다() {
        //given
        Product product1 = Product.builder()
                .id(1L)
                .name("name1")
                .price(new BigDecimal("10000"))
                .stockQuantity(100)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("name2")
                .price(new BigDecimal("20000"))
                .stockQuantity(200)
                .build();
        Product product3 = Product.builder()
                .id(3L)
                .name("name3")
                .price(new BigDecimal("30000"))
                .stockQuantity(300)
                .build();

        given(productRepository.findAll())
                .willReturn(List.of(product1, product2, product3));

        //when
        List<ProductResponse> productList = productService.getProductList();

        //then
        assertEquals(3, productList.size());

        ProductResponse productResponse1 = productList.get(0);
        assertEquals(1L, productResponse1.productId());
        assertEquals("name1", productResponse1.name());
        assertEquals(new BigDecimal("10000"), productResponse1.price());

        ProductResponse productResponse2 = productList.get(1);
        assertEquals(2L, productResponse2.productId());
        assertEquals("name2", productResponse2.name());
        assertEquals(new BigDecimal("20000"), productResponse2.price());

        ProductResponse productResponse3 = productList.get(2);
        assertEquals(3L, productResponse3.productId());
        assertEquals("name3", productResponse3.name());
        assertEquals(new BigDecimal("30000"), productResponse3.price());
    }

    @Test
    void 존재하지_않는_상품의_상세_정보를_조회하면_예외가_발생한다() {
        //given
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(ProductNotFoundException.class,
                // when
                () -> productService.getProductDetails(1L));
    }

    @Test
    void 상품의_상세_정보를_조회한다() {
        //given
        Product product = Product.builder()
                .id(1L)
                .name("name")
                .price(new BigDecimal("10000"))
                .stockQuantity(100)
                .build();
        given(productRepository.findById(1L))
                .willReturn(Optional.of(product));

        //when
        ProductDetails productDetails = productService.getProductDetails(1L);

        //then
        assertEquals(1L, productDetails.productId());
        assertEquals("name", productDetails.name());
        assertEquals(new BigDecimal("10000"), productDetails.price());
        assertEquals(100, productDetails.stockQuantity());
    }

    @Test
    void 존재하지_않는_상품을_구매하면_예외가_발생한다() {
        //given
        List<ProductPurchaseFeignRequest> productPurchaseFeignRequests = List.of(
                new ProductPurchaseFeignRequest(1L, 1)
        );
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(ProductNotFoundException.class,
                //when
                () -> productService.purchase(productPurchaseFeignRequests));
    }

    @Test
    void 상품을_구매한다() {
        //given
        List<ProductPurchaseFeignRequest> productPurchaseFeignRequests = List.of(
                new ProductPurchaseFeignRequest(1L, 1),
                new ProductPurchaseFeignRequest(2L, 2)
        );
        Product product1 = Product.builder()
                .id(1L)
                .price(new BigDecimal("10000"))
                .stockQuantity(10)
                .name("name1")
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .price(new BigDecimal("20000"))
                .stockQuantity(10)
                .name("name2")
                .build();

        given(productRepository.findById(1L))
                .willReturn(Optional.of(product1));
        given(productRepository.findById(2L))
                .willReturn(Optional.of(product2));

        //when
        List<ProductPurchaseFeignResponse> productPurchaseFeignRespons = productService.purchase(productPurchaseFeignRequests);

        //then
        assertEquals(8, product2.getStockQuantity());
        assertEquals(9, product1.getStockQuantity());

        assertEquals(2, productPurchaseFeignRespons.size());

        ProductPurchaseFeignResponse productPurchaseFeignResponse1 = productPurchaseFeignRespons.get(0);
        assertEquals(1L, productPurchaseFeignResponse1.productId());
        assertEquals(1, productPurchaseFeignResponse1.quantity());
        assertEquals("name1", productPurchaseFeignResponse1.productName());
        assertEquals(new BigDecimal("10000"), productPurchaseFeignResponse1.purchaseAmount());

        ProductPurchaseFeignResponse productPurchaseFeignResponse2 = productPurchaseFeignRespons.get(1);
        assertEquals(2L, productPurchaseFeignResponse2.productId());
        assertEquals(2, productPurchaseFeignResponse2.quantity());
        assertEquals("name2", productPurchaseFeignResponse2.productName());
        assertEquals(new BigDecimal("40000"), productPurchaseFeignResponse2.purchaseAmount());
    }

    @Test
    void 존재하지_않는_상품의_재고를_복구하면_예외가_발생한다() {
        //given
        List<ProductRestoreStockFeignRequest> requests = List.of(
                new ProductRestoreStockFeignRequest(1L, 1),
                new ProductRestoreStockFeignRequest(2L, 2)
        );
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(ProductNotFoundException.class,
                // when
                () -> productService.restoreStock(requests));
    }

    @Test
    void 상품_재고를_복구한다() {
        //given
        List<ProductRestoreStockFeignRequest> requests = List.of(
                new ProductRestoreStockFeignRequest(1L, 1),
                new ProductRestoreStockFeignRequest(2L, 2)
        );
        Product product1 = Product.builder()
                .id(1L)
                .price(new BigDecimal("10000"))
                .stockQuantity(10)
                .name("name1")
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .price(new BigDecimal("20000"))
                .stockQuantity(10)
                .name("name2")
                .build();

        given(productRepository.findById(1L))
                .willReturn(Optional.of(product1));
        given(productRepository.findById(2L))
                .willReturn(Optional.of(product2));

        //when
        productService.restoreStock(requests);

        //then
        assertEquals(11, product1.getStockQuantity());
        assertEquals(12, product2.getStockQuantity());
    }

    @Test
    void 존재하지_않는_특정_상품을_조회하면_예외가_발생한다() {
        //given
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(ProductNotFoundException.class,
                //when
                () -> productService.findById(1L)
        );

    }

    @Test
    void 특정_상품을_조회한다() {
        //given
        Product product1 = Product.builder()
                .id(1L)
                .price(new BigDecimal("10000"))
                .name("name1")
                .build();

        given(productRepository.findById(1L))
                .willReturn(Optional.of(product1));

        //when
        ProductFeignResponse productFeignResponse = productService.findById(1L);

        //then
        assertEquals(1L, productFeignResponse.productId());
        assertEquals("name1", productFeignResponse.name());
        assertEquals(new BigDecimal("10000"), productFeignResponse.price());
    }
}