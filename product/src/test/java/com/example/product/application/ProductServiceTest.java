package com.example.product.application;

import com.example.product.domain.AmountCalculator;
import com.example.product.domain.Money;
import com.example.product.domain.Product;
import com.example.product.dto.*;
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

    @Mock
    AmountCalculator calculator;

    @InjectMocks
    ProductService productService;

    @Test
    void 존재하지_않는_상품의_상세_정보를_조회하면_예외가_발생한다() {
        //given
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(IllegalArgumentException.class,
                // when
                () -> productService.getProductDetails(1L));
    }

    @Test
    void 상품_상세_정보_조회() {
        //given
        Product product = Product.builder()
                .id(1L)
                .name("name")
                .price(Money.of("10000"))
                .stockQuantity(100)
                .build();
        given(productRepository.findById(1L))
                .willReturn(Optional.of(product));

        //when
        ProductDetails productDetails = productService.getProductDetails(1L);

        //then
        assertEquals(1L, productDetails.productId());
        assertEquals("name", productDetails.name());
        assertEquals("10000", productDetails.price());
        assertEquals(100, productDetails.stockQuantity());
    }

    @Test
    void 상품_목록_조회() {
        //given
        Product product1 = Product.builder()
                .id(1L)
                .name("name1")
                .price(Money.of("10000"))
                .stockQuantity(100)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("name2")
                .price(Money.of("20000"))
                .stockQuantity(200)
                .build();
        Product product3 = Product.builder()
                .id(3L)
                .name("name3")
                .price(Money.of("30000"))
                .stockQuantity(300)
                .build();

        given(productRepository.findAll())
                .willReturn(List.of(product1, product2, product3));

        //when
        List<ProductDto> productList = productService.getProductList();

        //then
        assertEquals(3, productList.size());

        assertEquals(1L, productList.get(0).productId());
        assertEquals("name1", productList.get(0).name());
        assertEquals(new BigDecimal("10000"), productList.get(0).price());

        assertEquals(2L, productList.get(1).productId());
        assertEquals("name2", productList.get(1).name());
        assertEquals(new BigDecimal("20000"), productList.get(1).price());

        assertEquals(3L, productList.get(2).productId());
        assertEquals("name3", productList.get(2).name());
        assertEquals(new BigDecimal("30000"), productList.get(2).price());
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
                .price(Money.of("10000"))
                .stockQuantity(10)
                .name("name1")
                .build();
        given(productRepository.findById(1L))
                .willReturn(Optional.of(product1));
        Product product2 = Product.builder()
                .id(2L)
                .price(Money.of("20000"))
                .stockQuantity(10)
                .name("name2")
                .build();
        given(productRepository.findById(2L))
                .willReturn(Optional.of(product2));
        given(calculator.calculateAmount(product1, 1))
                .willReturn(Money.of("10000"));
        given(calculator.calculateAmount(product2, 2))
                .willReturn(Money.of("40000"));
        //when
        List<ProductPurchaseFeignResponse> productPurchaseFeignRespons = productService.purchase(productPurchaseFeignRequests);
        //then
        assertEquals(2, productPurchaseFeignRespons.size());
        assertEquals(1L, productPurchaseFeignRespons.getFirst().productId());
        assertEquals(1, productPurchaseFeignRespons.getFirst().quantity());
        assertEquals("name1", productPurchaseFeignRespons.get(0).productName());
        assertEquals(new BigDecimal("10000"), productPurchaseFeignRespons.get(0).purchaseAmount());
        assertEquals(2L, productPurchaseFeignRespons.get(1).productId());
        assertEquals(2, productPurchaseFeignRespons.get(1).quantity());
        assertEquals("name2", productPurchaseFeignRespons.get(1).productName());
        assertEquals(new BigDecimal("40000"), productPurchaseFeignRespons.get(1).purchaseAmount());
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
        assertThrows(IllegalArgumentException.class,
                //when
                () -> productService.purchase(productPurchaseFeignRequests));
    }

    @Test
    void 재고_복구_시_존재하지_않는_상품이면_예외가_발생한다() {
        //given
        List<ProductRestoreStockFeignRequest> requests = List.of(
                new ProductRestoreStockFeignRequest(1L, 2),
                new ProductRestoreStockFeignRequest(2L, 2)
        );
        given(productRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(IllegalArgumentException.class,
                // when
                () -> productService.restockStock(requests));
    }
}