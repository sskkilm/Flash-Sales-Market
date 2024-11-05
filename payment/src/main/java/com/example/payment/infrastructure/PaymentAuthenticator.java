package com.example.payment.infrastructure;

import com.example.payment.dto.PGPaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentAuthenticator {
    public boolean authenticate(PGPaymentRequest request) {
        if (request.paymentInfo().canAuthenticated()) {
            return true;
        }

        return false;
    }
}
