package com.example.payment.application;

import com.example.payment.dto.PGConfirmRequest;
import com.example.payment.dto.PGConfirmResponse;
import com.example.payment.dto.PGInitRequest;
import com.example.payment.dto.PGInitResponse;

public interface PGService {

    PGInitResponse pgInit(PGInitRequest pgInitRequest);

    PGConfirmResponse pgConfirm(PGConfirmRequest request);

}
