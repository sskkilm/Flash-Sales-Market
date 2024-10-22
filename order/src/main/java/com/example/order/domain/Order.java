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
    public static final int RETURNABLE_PERIOD_AFTER_DELIVERY = 1;

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
        validateCanBeCancelled(holder);
        this.status = OrderStatus.ORDER_CANCELED;
    }

    public void returns(Long memberId, LocalDateTimeHolder holder) {
        validateOrderBy(memberId);
        validateCanBeReturned(holder);
        this.status = OrderStatus.RETURN_IN_PROGRESS;
    }

    public boolean isNotOrderedBy(Long memberId) {
        return !Objects.equals(this.memberId, memberId);
    }

    private void validateOrderBy(Long memberId) {
        if (isOrderedBy(memberId)) {
            return;
        }
        throw new IllegalArgumentException(
                "this order is not ordered by this member -> memberId: " + memberId
        );
    }

    private boolean isOrderedBy(Long memberId) {
        return Objects.equals(this.memberId, memberId);
    }

    private void validateCanBeCancelled(LocalDateTimeHolder holder) {
        if (isOrderCompleted() && isBeforeCancellablePeriod(holder)) {
            return;
        }
        throw new IllegalStateException("This order cannot be canceled");
    }

    private boolean isOrderCompleted() {
        return this.status == OrderStatus.ORDER_COMPLETED;
    }

    private boolean isBeforeCancellablePeriod(LocalDateTimeHolder holder) {
        return this.createdAt.plusDays(CANCELLATION_PERIOD).isAfter(holder.now());
    }

    private void validateCanBeReturned(LocalDateTimeHolder holder) {
        if (isAfterDelivery() && isBeforeReturnablePeriod(holder)) {
            return;
        }
        throw new IllegalStateException("This order cannot be returned");
    }

    private boolean isAfterDelivery() {
        return this.status == OrderStatus.DELIVERY_COMPLETED;
    }

    private boolean isBeforeReturnablePeriod(LocalDateTimeHolder holder) {
        return this.updatedAt.plusDays(RETURNABLE_PERIOD_AFTER_DELIVERY).isAfter(holder.now());
    }

}
