package com.incident.management.domain;

import com.incident.management.domain.exceptions.IncidentNotFoundException;
import com.incident.management.infrastructure.persistence.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncidentRelationshipServiceImp implements IncidentRelationshipService {
    private final IncidentRepository incidentRepository;

    @Override
    @Transactional
    public void markAsDuplicate(IncidentNumber duplicate, IncidentNumber original) {

        var source = getIncidentById(duplicate);
        var target = getIncidentById(original);

        source.duplicateOf(original);
        target.markAsBeingDuplicatedBy(duplicate);

        incidentRepository.save(source);
        incidentRepository.save(target);
        incidentRepository.flush();
    }

    @Override
    @Transactional
    public void markAsBlockedBy(IncidentNumber blocked, IncidentNumber blocker) {
        var blockedIncident = getIncidentById(blocked);
        var blockerIncident = getIncidentById(blocker);

        blockedIncident.blockedBy(blocker);
        blockerIncident.markAsBlockerFor(blocked);

        incidentRepository.save(blockedIncident);
        incidentRepository.save(blockerIncident);
        incidentRepository.flush();
    }

    @Override
    @Transactional
    public void markAsRelated(IncidentNumber source, IncidentNumber target) {
        var sourceIncident = getIncidentById(source);
        var targetIncident = getIncidentById(target);

        sourceIncident.relatedTo(target);
        targetIncident.relatedTo(source);

        incidentRepository.save(sourceIncident);
        incidentRepository.save(targetIncident);
        incidentRepository.flush();

    }

    @Override
    @Transactional
    public Incident getIncidentById(IncidentNumber incidentNumber) {
        return incidentRepository.findById(incidentNumber)
                .orElseThrow(() -> new IncidentNotFoundException(incidentNumber));
    }
}





















