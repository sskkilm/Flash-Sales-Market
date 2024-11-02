package com.example.product.domain;

import com.example.product.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void 재고가_부족하면_예외가_발생한다() {
        //given
        NormalProduct product = NormalProduct.builder()
                .stockQuantity(10)
                .build();

        //then
        assertThrows(InsufficientStockException.class,
                //when
                () -> product.checkOutOfStock(15));
    }

    @Test
    void 일반_상품_재고를_감소시킨다() {
        //given
        Product product = NormalProduct.builder()
                .stockQuantity(10)
                .build();

        //when
        product.decreaseStock(2);

        //then
        assertEquals(8, product.getStockQuantity());
    }

    @Test
    void 일반_상품의_재고를_증가시킨다() {
        //given
        Product product = NormalProduct.builder()
                .stockQuantity(5)
                .build();

        //when
        product.increaseStock(5);

        //then
        assertEquals(10, product.getStockQuantity());
    }

    @Test
    void 한정판_상품_재고를_감소시킨다() {
        //given
        Product product = LimitedProduct.builder()
                .stockQuantity(10)
                .build();

        //when
        product.decreaseStock(2);

        //then
        assertEquals(8, product.getStockQuantity());
    }

    @Test
    void 한정판_상품의_재고를_증가시킨다() {
        //given
        Product product = LimitedProduct.builder()
                .stockQuantity(5)
                .build();

        //when
        product.increaseStock(5);

        //then
        assertEquals(10, product.getStockQuantity());
    }
}