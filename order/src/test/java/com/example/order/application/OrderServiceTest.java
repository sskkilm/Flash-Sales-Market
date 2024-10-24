package com.example.order.application;

import com.example.member.application.MemberService;
import com.example.order.application.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MemberService memberService;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void 주문_취소시_존재하지_않는_주문이면_예외가_발생한다() {
        //given
        given(memberService.findById(1L))
                .willReturn(1L);
        given(orderRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(IllegalArgumentException.class,
                //when
                () -> orderService.cancel(1L, 1L));
    }

    @Test
    void 반품_시_존재하지_않는_주문이면_예외가_발생한다() {
        //given
        given(memberService.findById(1L))
                .willReturn(1L);
        given(orderRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(IllegalArgumentException.class,
                //when
                () -> orderService.returns(1L, 1L));
    }
}