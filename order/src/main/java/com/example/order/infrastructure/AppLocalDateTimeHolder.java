package com.example.order.infrastructure;

import com.example.order.application.timeholder.LocalDateTimeHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppLocalDateTimeHolder implements LocalDateTimeHolder {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
