package com.example.order.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class Order {
    private Long id;
    private Long memberId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order create(Long memberId) {
        return Order.builder()
                .memberId(memberId)
                .status(OrderStatus.ORDER_COMPLETED)
                .build();
    }

    public void cancel(Long memberId) {
        validateOrderBy(memberId);
        validateBeforeDelivery();
        this.status = OrderStatus.ORDER_CANCELED;
    }

    private void validateOrderBy(Long memberId) {
        if (isNotOrderedBy(memberId)) {
            throw new IllegalArgumentException(
                    "this order is not ordered by this member -> memberId: " + memberId
            );
        }
    }

    private void validateBeforeDelivery() {
        if (afterDelivery()) {
            throw new IllegalArgumentException("order can not be canceled");
        }
    }

    private boolean isNotOrderedBy(Long memberId) {
        return !Objects.equals(this.memberId, memberId);
    }

    private boolean afterDelivery() {
        return this.status != OrderStatus.ORDER_COMPLETED;
    }

}
