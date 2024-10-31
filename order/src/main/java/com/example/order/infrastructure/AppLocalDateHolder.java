package com.example.order.infrastructure;

import com.example.order.application.timeholder.LocalDateHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AppLocalDateHolder implements LocalDateHolder {
    @Override
    public LocalDate now() {
        return LocalDate.now();
    }
}
