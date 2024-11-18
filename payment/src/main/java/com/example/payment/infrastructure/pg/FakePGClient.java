package com.example.payment.infrastructure.pg;

import com.example.payment.application.port.PGClient;
import com.example.payment.dto.PGConfirmRequest;
import com.example.payment.dto.PGConfirmResponse;
import com.example.payment.dto.PGInitRequest;
import com.example.payment.dto.PGInitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FakePGClient implements PGClient {

    private final ThirdPartyPaymentService thirdPartyPaymentService;
    private final FakePGRepository fakePgRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PGInitResponse pgInit(PGInitRequest request) {

        boolean isAuthenticated = thirdPartyPaymentService.authenticate(request.memberPaymentInfo());
        if (!isAuthenticated) {
            throw new PGServiceException("결제 인증 실패");
        }

        String paymentKey = UUID.randomUUID().toString();
        PGEntity pgEntity = fakePgRepository.findByOrderId(request.orderId())
                .map(entity -> entity.updatePaymentKey(paymentKey))
                .orElseGet(() -> PGEntity.create(request.orderId(), request.amount(), paymentKey));
        fakePgRepository.save(pgEntity);

        return new PGInitResponse(paymentKey, request.orderId(), request.amount(), request.memberPaymentInfo());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PGConfirmResponse pgConfirm(PGConfirmRequest request) {

        PGEntity pgEntity = fakePgRepository.findByPaymentKey(request.paymentKey())
                .orElseThrow(() -> new PGServiceException("존재하지 않는 결제 정보입니다."));
        pgEntity.validate(request.orderId(), request.amount());

        boolean isPaid = thirdPartyPaymentService.pay(request.memberPaymentInfo());
        if (!isPaid) {
            throw new PGServiceException("결제 실패");
        }

        return new PGConfirmResponse(request.orderId(), request.amount(), request.paymentKey());
    }
}
