package com.incident.management.domain;

import com.incident.management.infrastructure.persistence.IncidentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import static com.incident.management.domain.Incident.*;
import static com.incident.management.domain.Incident.RelatedIncident.*;
import static com.incident.management.domain.Incident.RelatedIncident.RelationshipType.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class IncidentRelationshipServiceTest {

    @Autowired
    private IncidentRepository incidentRepository;
    @Mock
    private Clock clock;
    private IncidentFactory incidentFactory=null;
    private IncidentRelationshipService incidentRelationshipService=null;

    @BeforeEach
    void setUp() {
        incidentRelationshipService=new IncidentRelationshipServiceImp(incidentRepository);
        incidentFactory=new IncidentFactory(clock);
    }

    @Test
    @Transactional
    void should_mark_as_duplicate() {

        //given:
        var source = incidentFactory.newIncident(IncidentDTO.builder()
                .title("Incident1")
                .createdBy(new User(1L, "s", "ss"))
                .description("Incident Description")
                .priority(Priority.CRITICAL)
                .build());
        var target = incidentFactory.newIncident(IncidentDTO.builder()
                .title("Incident1")
                .createdBy(new User(1L, "s", "ss"))
                .description("Incident Description")
                .priority(Priority.CRITICAL)
                .build());

        var storedSource = incidentRepository.save(source);
        var storedTarget = incidentRepository.save(target);

        //when:
        incidentRelationshipService.markAsDuplicate(storedSource.getId(), storedTarget.getId());


        //then:
        var duplicatesIncident = incidentRelationshipService.getIncidentById(storedSource.getId());
        var duplicatedByTarget = incidentRelationshipService.getIncidentById(storedTarget.getId());

        assertThat(duplicatesIncident.hasRelationshipTo(storedTarget.getId(), DUPLICATES))
                .isTrue();
        assertThat(duplicatedByTarget.hasRelationshipTo(storedSource.getId(), DUPLICATED_BY))
                .isTrue();
    }

    @Test
    void should_mark_as_blocked_by() {
        //given:
        var source = incidentFactory.newIncident(IncidentDTO.builder()
                .title("Incident1")
                .createdBy(new User(1L, "s", "ss"))
                .description("Incident Description")
                .priority(Priority.CRITICAL)
                .build());
        var target = incidentFactory.newIncident(IncidentDTO.builder()
                .title("Incident1")
                .createdBy(new User(1L, "s", "ss"))
                .description("Incident Description")
                .priority(Priority.CRITICAL)
                .build());

        var storedSource = incidentRepository.save(source);
        var storedTarget = incidentRepository.save(target);

        //when:
        incidentRelationshipService.markAsBlockedBy(storedSource.getId(), storedTarget.getId());

        //then:
        var blockedIncident = incidentRelationshipService.getIncidentById(storedSource.getId());
        var blockerIncident = incidentRelationshipService.getIncidentById(storedTarget.getId());
        assertThat(blockedIncident.hasRelationshipTo(blockerIncident.getId(), BLOCKED_BY))
                .isTrue();
        assertThat(blockerIncident.hasRelationshipTo(blockedIncident.getId(), BLOCKER))
                .isTrue();

    }
    @Test
    void should_mark_as_related() {
        //given:
        var source = incidentFactory.newIncident(IncidentDTO.builder()
                .title("Incident1")
                .createdBy(new User(1L, "s", "ss"))
                .description("Incident Description")
                .priority(Priority.CRITICAL)
                .build());
        var target = incidentFactory.newIncident(IncidentDTO.builder()
                .title("Incident1")
                .createdBy(new User(1L, "s", "ss"))
                .description("Incident Description")
                .priority(Priority.CRITICAL)
                .build());

        var storedSource = incidentRepository.save(source);
        var storedTarget = incidentRepository.save(target);

        //when:
        incidentRelationshipService.markAsRelated(storedSource.getId(), storedTarget.getId());

        //then:
        var from = incidentRelationshipService.getIncidentById(storedSource.getId());
        var to = incidentRelationshipService.getIncidentById(storedTarget.getId());
        assertThat(from.hasRelationshipTo(to.getId(), RELATED_TO))
                .isTrue();
        assertThat(to.hasRelationshipTo(from.getId(), RELATED_TO))
                .isTrue();

    }
}