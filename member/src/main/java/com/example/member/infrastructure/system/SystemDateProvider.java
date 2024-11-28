package com.example.member.infrastructure.system;

import com.example.member.application.port.DateProvider;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SystemDateProvider implements DateProvider {
    @Override
    public Date now() {
        return new Date();
    }
}
