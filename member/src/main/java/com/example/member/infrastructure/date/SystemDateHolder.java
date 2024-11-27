package com.example.member.infrastructure.date;

import com.example.member.application.port.DateHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SystemDateHolder implements DateHolder {
    @Override
    public Date now() {
        return new Date();
    }
}
