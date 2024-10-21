package com.example.product.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void 상품의_재고가_감소한다() {
        //given
        Product product = Product.builder()
                .stockQuantity(10)
                .build();

        //when
        product.decreaseStock(2);

        //then
        assertEquals(8, product.getStockQuantity());
    }

    @Test
    void 상품의_재고가_부족하면_예외가_발생한다() {
        //given
        Product product = Product.builder()
                .stockQuantity(3)
                .build();
        //then
        assertThrows(IllegalArgumentException.class,
                //when
                () -> product.decreaseStock(5));
    }
}