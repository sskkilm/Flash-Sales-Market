package com.example.order.domain;

import com.example.order.exception.CanNotBeCanceledException;
import com.example.order.exception.CanNotBeReturnedException;
import com.example.order.exception.OrderMemberUnmatchedException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.order.domain.OrderStatus.*;

@Getter
@Builder
public class Order {
    private static final int CANCELLABLE_PERIOD_AFTER_ORDER = 1;
    private static final int RETURNABLE_PERIOD_AFTER_DELIVERY = 1;

    private Long id;
    private Long memberId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order create(Long memberId) {
        return Order.builder()
                .memberId(memberId)
                .status(IN_PROGRESS)
                .build();
    }

    public void waitingForPayment() {
        this.status = WAITING_FOR_PAYMENT;
    }

    public void cancel(Long memberId, LocalDateTime canceledDateTime) {
        validateOrderBy(memberId);
        validateCanBeCancelled(canceledDateTime);
        this.status = CANCELED;
    }

    public void returns(Long memberId, LocalDateTime returnedDateTime) {
        validateOrderBy(memberId);
        validateCanBeReturned(returnedDateTime);
        this.status = RETURN_IN_PROGRESS;
    }

    public void returned() {
        this.status = RETURNED;
    }

    private void validateOrderBy(Long memberId) {
        if (isOrderedBy(memberId)) {
            return;
        }
        throw new OrderMemberUnmatchedException(
                "this order is not ordered by this member -> memberId: " + memberId
        );
    }

    private boolean isOrderedBy(Long memberId) {
        return Objects.equals(this.memberId, memberId);
    }

    private void validateCanBeCancelled(LocalDateTime canceledDateTime) {
        if (isCompleted() && isBeforeCancellablePeriod(canceledDateTime)) {
            return;
        }
        throw new CanNotBeCanceledException("This order cannot be canceled");
    }

    private boolean isCompleted() {
        return this.status == COMPLETED;
    }

    private boolean isBeforeCancellablePeriod(LocalDateTime canceledDateTime) {
        return this.createdAt.plusDays(CANCELLABLE_PERIOD_AFTER_ORDER).isAfter(canceledDateTime);
    }

    private void validateCanBeReturned(LocalDateTime returnedDateTime) {
        if (isDelivered() && isBeforeReturnablePeriod(returnedDateTime)) {
            return;
        }
        throw new CanNotBeReturnedException("This order cannot be returned");
    }

    private boolean isDelivered() {
        return this.status == DELIVERED;
    }

    private boolean isBeforeReturnablePeriod(LocalDateTime returnedDateTime) {
        return this.updatedAt.plusDays(RETURNABLE_PERIOD_AFTER_DELIVERY).isAfter(returnedDateTime);
    }

}
