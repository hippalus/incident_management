package com.incident.management.domain;

public interface ChainStatus {
    default IncidentStatus assign() {
        return IncidentStatus.ASSIGNED;
    }

    default IncidentStatus resolve() {
        return IncidentStatus.RESOLVED;
    }

    default IncidentStatus close() {
        return IncidentStatus.CLOSED;
    }

    default IncidentStatus reopen() {
        return IncidentStatus.OPEN;
    }

    static IncidentStatus cannotTransitTo(IncidentStatus source, IncidentStatus target) {
        throw new IllegalStateException(String.format("Cannot transit from %s to %s!", source, target));
    }


}
