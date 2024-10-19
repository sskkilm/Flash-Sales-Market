package com.example.product.application;

import com.example.product.domain.Money;
import com.example.product.domain.Product;
import com.example.product.dto.ProductDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        given(productRepository.findById(1L))
                .willReturn(Optional.of(
                        Product.builder()
                                .id(1L)
                                .name("name")
                                .price(Money.of(new BigDecimal("10000")))
                                .stockQuantity(100)
                                .build()
                ));

        //when
        ProductDetails productDetails = productService.getProductDetails(1L);

        //then
        assertEquals(1L, productDetails.productId());
        assertEquals("name", productDetails.name());
        assertEquals("10000", productDetails.price());
        assertEquals(100, productDetails.stockQuantity());
    }
}