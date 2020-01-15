package com.incident.management.domain;

public interface ChainStatus {
    default IncidentStatus assign() {
        return IncidentStatus.ASSIGNED;
    }

    default IncidentStatus resolved(){
        return IncidentStatus.RESOLVED;
    }
}
