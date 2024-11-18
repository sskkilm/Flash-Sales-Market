package com.example.payment.application.port;

import com.example.payment.dto.PGConfirmRequest;
import com.example.payment.dto.PGConfirmResponse;
import com.example.payment.dto.PGInitRequest;
import com.example.payment.dto.PGInitResponse;

public interface PGClient {

    PGInitResponse pgInit(PGInitRequest pgInitRequest);

    PGConfirmResponse pgConfirm(PGConfirmRequest request);

}
