package com.example.payment.infrastructure.external;

import com.example.payment.application.PGService;
import com.example.payment.dto.PGConfirmRequest;
import com.example.payment.dto.PGConfirmResponse;
import com.example.payment.dto.PGInitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PGServiceImpl implements PGService {

    private final ThirdPartyPaymentService thirdPartyPaymentService;
    private final PGRepository pgRepository;

    @Override
    public void init(PGInitRequest request) {

        boolean isAuthenticated = thirdPartyPaymentService.authenticate(request.memberPaymentInfo());
        if (!isAuthenticated) {
            throw new PGServiceException("결제 진입 실패");
        }

        String paymentKey = UUID.randomUUID().toString();
        pgRepository.save(PGEntity.create(request.orderId(), request.amount(), paymentKey));

        redirectToConfirm();
    }

    @Override
    public PGConfirmResponse confirm(PGConfirmRequest request) {

        PGEntity pgEntity = pgRepository.findByPaymentKey(request.paymentKey())
                .orElseThrow(() -> new PGServiceException("존재하지 않는 결제 정보입니다."));
        pgEntity.validate(request.orderId(), request.amount());

        boolean isPaid = thirdPartyPaymentService.pay(request.memberPaymentInfo());
        if (!isPaid) {
            throw new PGServiceException("결제 승인 실패");
        }

        return new PGConfirmResponse(request.orderId(), request.amount(), request.paymentKey());
    }

    private void redirectToConfirm() {
    }
}
