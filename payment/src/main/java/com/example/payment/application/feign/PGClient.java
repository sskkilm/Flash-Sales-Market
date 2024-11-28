package com.example.payment.application.feign;

import com.example.payment.common.dto.request.PGConfirmRequest;
import com.example.payment.common.dto.response.PGConfirmResponse;

public interface PGClient {

    PGConfirmResponse pgConfirm(PGConfirmRequest request, boolean flag);
}
