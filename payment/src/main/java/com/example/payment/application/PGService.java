package com.example.payment.application;

import com.example.payment.dto.PGConfirmRequest;
import com.example.payment.dto.PGConfirmResponse;
import com.example.payment.dto.PGInitRequest;

public interface PGService {

    void init(PGInitRequest pgInitRequest);

    PGConfirmResponse confirm(PGConfirmRequest request);

}
