package com.example.product.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTest {

    @Test
    void String_객체를_Money_객체로_변환한다() {
        //given
        //when
        Money money = Money.of("10000");

        //then
        assertEquals(new Money(new BigDecimal("10000")), money);
    }

    @Test
    void Money_객체의_곱하기_연산() {
        //given
        Money money = Money.of("10000");

        //when
        Money multiplidMoney = money.multiply(3);

        //then
        assertEquals(Money.of("30000"), multiplidMoney);
    }

}