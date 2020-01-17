package com.incident.management.domain;

import lombok.Value;

import java.io.Serializable;

@Value
public class IncidentNumber implements Serializable {

    private final String value;

    private IncidentNumber(String value) {

        this.value = value;
    }

    public static IncidentNumber of(String value) {
        return new IncidentNumber(value);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
