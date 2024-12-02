package com.example.payment.infrastructure.repository;

import com.example.payment.application.port.PaymentRepository;
import com.example.payment.domain.Payment;
import com.example.payment.domain.exception.PaymentServiceException;
import com.example.payment.infrastructure.repository.persistence.PaymentJpaRepository;
import com.example.payment.infrastructure.repository.persistence.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.payment.domain.exception.ErrorCode.PAYMENT_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return PaymentMapper.toModel(
                paymentJpaRepository.save(
                        PaymentMapper.toEntity(payment)
                )
        );
    }

    @Override
    public Payment findByOrderId(Long orderId) {
        return PaymentMapper.toModel(
                paymentJpaRepository.findByOrderId(orderId)
                        .orElseThrow(() -> new PaymentServiceException(PAYMENT_NOT_FOUND))
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void rollBack(Payment payment) {
        paymentJpaRepository.delete(PaymentMapper.toEntity(payment));
    }

    @Override
    public List<Payment> findByOrderIdInOrderIds(List<Long> orderIds) {
        return paymentJpaRepository.findByOrderIdInOrderIds(orderIds)
                .stream()
                .map(PaymentMapper::toModel)
                .toList();
    }
}
