package com.incident.management.domain;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Value
public class IncidentNumber implements Serializable {

    private final String value;

    private IncidentNumber(String value) {

        this.value = value;
    }

    public static IncidentNumber of(String value) {
        return new IncidentNumber(value);
    }

    public static IncidentNumber next() {
        return new IncidentNumber(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return getValue();
    }
}
