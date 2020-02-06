package com.incident.management.domain;

import org.springframework.transaction.annotation.Transactional;

public interface IncidentRelationshipService {
    @Transactional
    void markAsDuplicate(IncidentNumber duplicate, IncidentNumber original);
    @Transactional
    Incident getIncidentById(IncidentNumber incidentNumber);
    @Transactional
    void markAsBlockedBy(IncidentNumber blocked, IncidentNumber blocker);
    @Transactional
    void markAsRelated(IncidentNumber source, IncidentNumber target);

}
