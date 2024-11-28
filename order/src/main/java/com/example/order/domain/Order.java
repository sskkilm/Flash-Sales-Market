package com.example.order.domain;

import com.example.order.domain.exception.OrderServiceException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.order.domain.OrderStatus.*;
import static com.example.order.domain.exception.ErrorCode.MEMBER_UN_MATCHED;

@Getter
@Builder
public class Order {
    private static final int CANCELLABLE_PERIOD_AFTER_ORDER = 1;

    private Long id;
    private Long memberId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order create(Long memberId) {
        return Order.builder()
                .memberId(memberId)
                .status(PENDING_PAYMENT)
                .build();
    }

    public void cancel(Long memberId) {
        validateOrderBy(memberId);
        this.status = CANCELED;
    }

    public boolean isNotOrderBy(Long memberId) {
        return !Objects.equals(this.memberId, memberId);
    }

    public boolean isNotPending() {
        return this.status != PENDING_PAYMENT;
    }

    private void validateOrderBy(Long memberId) {
        if (isOrderedBy(memberId)) {
            return;
        }
        throw new OrderServiceException(MEMBER_UN_MATCHED);
    }

    private boolean isOrderedBy(Long memberId) {
        return Objects.equals(this.memberId, memberId);
    }

}
