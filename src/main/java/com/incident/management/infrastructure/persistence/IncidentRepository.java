package com.incident.management.infrastructure.persistence;

import com.incident.management.domain.Incident;
import com.incident.management.domain.IncidentNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, IncidentNumber> {
}
