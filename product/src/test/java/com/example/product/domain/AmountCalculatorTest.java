package com.example.product.domain;

import com.example.common.domain.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmountCalculatorTest {

    @Test
    void 주문된_상품의_금액을_계산한다() {
        //given
        AmountCalculator calculator = new AmountCalculator();
        Product product = Product.builder()
                .price(Money.of("10000"))
                .build();

        //when
        Money money = calculator.calculateAmount(product, 3);

        //then
        assertEquals(Money.of("30000"), money);
    }

}