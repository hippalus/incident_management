package com.incident.management.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SystemClock implements Clock {
    @Override
    public LocalDateTime time() {
        return LocalDateTime.now();
    }
}
