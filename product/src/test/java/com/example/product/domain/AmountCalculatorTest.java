package com.example.product.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmountCalculatorTest {

    @Test
    void 상품의_금액을_계산한다() {
        //given
        AmountCalculator calculator = new AmountCalculator();
        Product product = Product.builder()
                .price(new BigDecimal("10000"))
                .build();

        //when
        BigDecimal calculatedAmount = calculator.calculateAmount(product, 3);

        //then
        assertEquals(new BigDecimal("30000"), calculatedAmount);
    }

}