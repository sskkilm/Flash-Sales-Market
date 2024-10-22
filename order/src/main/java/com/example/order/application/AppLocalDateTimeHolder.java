package com.example.order.application;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppLocalDateTimeHolder implements LocalDateTimeHolder{
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
