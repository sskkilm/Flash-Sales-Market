package com.example.product.infrastructure;

import com.example.product.application.port.LocalDateTimeHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppLocalDateTimeHolder implements LocalDateTimeHolder {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
