package com.incident.management.domain;

public interface ChainStatus {
    default IncidentStatus assign() {
        return IncidentStatus.ASSIGNED;
    }

    default IncidentStatus resolved() {
        return IncidentStatus.RESOLVED;
    }

    default IncidentStatus close() {
        return IncidentStatus.CLOSED;
    }

    default IncidentStatus reopen() {
        return IncidentStatus.OPEN;
    }
}
