package com.example.payment.infrastructure.pg;

import com.example.payment.application.feign.PGClient;
import com.example.payment.common.dto.request.PGConfirmRequest;
import com.example.payment.common.dto.response.PGConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FakePGClient implements PGClient {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PGConfirmResponse pgConfirm(PGConfirmRequest request, boolean flag) {
        if (!flag) {
            throw new RuntimeException("결제 승인 실패");
        }

        return new PGConfirmResponse(request.paymentKey(), request.orderId(), request.amount());
    }
}
