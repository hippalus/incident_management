package com.incident.management.domain;

import java.time.Duration;


public class Category {
    private Long id;
    private String name;
    private Duration duration;

    public Category(Long id, String name, Duration duration) {

        this.id = id;
        this.name = name;
        this.duration = duration;
    }
}
