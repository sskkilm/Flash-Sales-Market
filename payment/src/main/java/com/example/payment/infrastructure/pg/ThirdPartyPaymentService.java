package com.example.payment.infrastructure.pg;

import com.example.payment.common.dto.MemberPaymentInfo;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyPaymentService {
    public boolean authenticate(MemberPaymentInfo memberPaymentInfo) {
        if (memberPaymentInfo.canInitiated()) {
            return true;
        }

        return false;
    }

    public boolean pay(MemberPaymentInfo memberPaymentInfo) {
        if (memberPaymentInfo.canConfirmed()) {
            return true;
        }

        return false;
    }
}
