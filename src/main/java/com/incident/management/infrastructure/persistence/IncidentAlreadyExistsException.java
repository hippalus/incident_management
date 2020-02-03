package com.incident.management.infrastructure.persistence;

public class IncidentAlreadyExistsException extends RuntimeException {

    public IncidentAlreadyExistsException(String message) {
        super(message);
    }
}
