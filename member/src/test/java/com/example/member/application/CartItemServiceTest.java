package com.example.member.application;

import com.example.member.domain.CartItem;
import com.example.member.dto.CartItemUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @InjectMocks
    CartItemService cartItemService;

    @Mock
    CartItemRepository cartItemRepository;

    @Test
    void 장바구니_항목_수정_시_존재하지_않는_장바구니_항목이면_예외가_발생한다() {
        //given
        CartItemUpdateRequest request = new CartItemUpdateRequest(1);
        given(cartItemRepository.findById(1L))
                .willThrow(new IllegalArgumentException());
        //then
        assertThrows(IllegalArgumentException.class,
                // when
                () -> cartItemService.update(1L, 1L, request));
    }

    @Test
    void 장바구니_항목_수정_시_회원_정보가_다르면_예외가_발생한다() {
        //given
        CartItemUpdateRequest request = new CartItemUpdateRequest(1);
        given(cartItemRepository.findById(1L))
                .willReturn(
                        CartItem.builder()
                                .id(1L)
                                .memberId(2L)
                                .build()
                );
        //then
        assertThrows(IllegalArgumentException.class,
                // when
                () -> cartItemService.update(1L, 1L, request));
    }

    @Test
    void 장바구니_항목_삭제_시_존재하지_않는_장바구니_항목이면_예외가_발생한다() {
        //given
        given(cartItemRepository.findById(1L))
                .willThrow(new IllegalArgumentException());
        //then
        assertThrows(IllegalArgumentException.class,
                // when
                () -> cartItemService.delete(1L, 1L));
    }

    @Test
    void 장바구니_항목_삭제_시_회원_정보가_다르면_예외가_발생한다() {
        //given
        given(cartItemRepository.findById(1L))
                .willReturn(
                        CartItem.builder()
                                .id(1L)
                                .memberId(2L)
                                .build()
                );
        //then
        assertThrows(IllegalArgumentException.class,
                // when
                () -> cartItemService.delete(1L, 1L));
    }
}