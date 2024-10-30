package com.example.product.domain;

import com.example.product.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void 상품의_재고를_감소시킨다() {
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
    void 상품의_재고를_감소시킬_때_남아있는_재고가_부족하면_예외가_발생한다() {
        //given
        Product product = Product.builder()
                .stockQuantity(3)
                .build();
        //then
        assertThrows(InsufficientStockException.class,
                //when
                () -> product.decreaseStock(5));
    }

    @Test
    void 상품의_재고를_증가시킨다() {
        //given
        Product product = Product.builder()
                .stockQuantity(5)
                .build();

        //when
        product.increaseStock(5);

        //then
        assertEquals(10, product.getStockQuantity());
    }
}