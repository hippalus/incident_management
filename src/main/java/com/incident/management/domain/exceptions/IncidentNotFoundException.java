package com.incident.management.domain.exceptions;

import com.incident.management.domain.IncidentNumber;

public class IncidentNotFoundException extends RuntimeException {
    public IncidentNotFoundException(IncidentNumber incidentNumber) {
        super(String.format("Incident %s not found", incidentNumber));
    }
}
