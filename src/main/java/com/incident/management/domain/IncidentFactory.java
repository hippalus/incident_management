package com.incident.management.domain;

import org.springframework.stereotype.Service;

@Service
public class IncidentFactory {

    private final Clock clock;

    public IncidentFactory(Clock clock) {
        this.clock = clock;
    }

    public Incident newIncident(IncidentDTO incidentDTO) {
        return Incident.builder()
                .withId(IncidentNumber.next())
                .withTitle(incidentDTO.getTitle())
                .withCreatedBy(incidentDTO.getCreatedBy())
                .withPriority(incidentDTO.getPriority())
                .withDescription(incidentDTO.getDescription())
                .withCreateAt(clock.time())
                .build();
    }


}
