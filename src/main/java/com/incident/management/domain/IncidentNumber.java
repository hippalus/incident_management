package com.incident.management.domain;

import lombok.Value;

@Value
public class IncidentNumber {
    private final String value;

    private IncidentNumber(String value) {

        this.value = value;
    }

    public static IncidentNumber of(String value) {
        return new IncidentNumber(value);
    }
}
