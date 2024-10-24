package com.example.member.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartItemTest {

    @Test
    void 장바구니_항목_추가() {
        //given
        Long memberId = 1L;
        Long productId = 1L;
        int quantity = 1;
        //when
        CartItem cartItem = CartItem.create(memberId, productId, quantity);
        //then
        assertEquals(1L, cartItem.getMemberId());
        assertEquals(1L, cartItem.getProductId());
        assertEquals(1, cartItem.getQuantity());
    }

    @Test
    void 장바구니_항목_수량_변경() {
        //given
        CartItem cartItem = CartItem.builder()
                .quantity(10)
                .build();

        //when
        cartItem.updateQuantity(5);

        //then
        assertEquals(5, cartItem.getQuantity());
    }

    @Test
    void 장바구니_회원_정보가_다르다() {
        //given
        CartItem cartItem = CartItem.builder()
                .memberId(1L)
                .build();

        //when
        boolean notIncludedBy = cartItem.isNotIncludedBy(2L);

        //then
        assertTrue(notIncludedBy);
    }
}