package com.example.payment.infrastructure;

import com.example.payment.application.PaymentRepository;
import com.example.payment.domain.Payment;
import com.example.payment.exception.PaymentServiceException;
import com.example.payment.infrastructure.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.payment.exception.error.ErrorCode.PAYMENT_INFO_DOES_NOT_EXIST;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(PaymentEntity.from(payment)).toModel();
    }

    @Override
    public boolean existsByOrderId(Long orderId) {
        return paymentJpaRepository.existsByOrderId(orderId);
    }

    @Override
    public Payment findByOrderId(Long orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .orElseThrow(
                        () -> new PaymentServiceException(PAYMENT_INFO_DOES_NOT_EXIST)
                ).toModel();
    }
}
