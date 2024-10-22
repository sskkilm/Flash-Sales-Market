package com.example.order.domain;

import com.example.order.application.LocalDateTimeHolder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class Order {
    public static final int CANCELLATION_PERIOD = 1;
    public static final int DELIVERY_PERIOD = 2;
    public static final int RETURNABLE_PERIOD = 3;

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

    public void cancel(Long memberId, LocalDateTimeHolder holder) {
        validateOrderBy(memberId);
        validateBeforeDelivery(holder);
        this.status = OrderStatus.ORDER_CANCELED;
    }

    public void returns(Long memberId, LocalDateTimeHolder holder) {
        validateOrderBy(memberId);
        validateAfterDeliveryCompleted(holder);
        validateCanBeReturned(holder);
        this.status = OrderStatus.RETURN_IN_PROGRESS;
    }

    private void validateAfterDeliveryCompleted(LocalDateTimeHolder holder) {
        if (isBeforeDeliveryCompleted(holder)) {
            throw new IllegalStateException("You can return it after the delivery is completed");
        }
    }

    private boolean isBeforeDeliveryCompleted(LocalDateTimeHolder holder) {
        return this.createdAt.plusDays(DELIVERY_PERIOD).isAfter(holder.now());
    }

    private void validateCanBeReturned(LocalDateTimeHolder holder) {
        if (isAfterReturnablePeriod(holder)) {
            throw new IllegalStateException("Return period has expired");
        }
    }

    private boolean isAfterReturnablePeriod(LocalDateTimeHolder holder) {
        return this.createdAt.plusDays(RETURNABLE_PERIOD).isBefore(holder.now());
    }

    private void validateOrderBy(Long memberId) {
        if (isNotOrderedBy(memberId)) {
            throw new IllegalArgumentException(
                    "this order is not ordered by this member -> memberId: " + memberId
            );
        }
    }

    private void validateBeforeDelivery(LocalDateTimeHolder holder) {
        if (isAfterCancellablePeriod(holder)) {
            throw new IllegalStateException("Cancellation period has expired");
        }
    }

    private boolean isNotOrderedBy(Long memberId) {
        return !Objects.equals(this.memberId, memberId);
    }

    private boolean isAfterCancellablePeriod(LocalDateTimeHolder holder) {
        return this.createdAt.plusDays(CANCELLATION_PERIOD).isBefore(holder.now());
    }
}
