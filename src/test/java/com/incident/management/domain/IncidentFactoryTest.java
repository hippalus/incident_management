package com.incident.management.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IncidentFactoryTest {
    @Mock
    Clock clock;

    private IncidentFactory incidentFactory;

    @BeforeEach
    void setUp() {
        incidentFactory = new IncidentFactory(clock);
    }

    @Test
    void should_create_incident_in_InitialState() {
        //when
        var incident = incidentFactory.newIncident(new IncidentDTO("Bug", "BlaBlaBla", Priority.MINOR, new User()));
        //then
        assertThat(incident.getStatus()).isEqualTo(IncidentStatus.OPEN);
    }

    @Test
    void should_assign_unit_number() {
        //when
        var incident1 = incidentFactory
                .newIncident(new IncidentDTO("Bug", "BlaBlaBla", Priority.MINOR, new User()));

        var incident2 = incidentFactory
                .newIncident(new IncidentDTO("Bug2", "BlaBlaBla2", Priority.BLOCKER, new User()));
        //then
        assertThat(incident1.getId()).isNotEqualTo(incident2.getId());
    }

    @Test
    void should_assign_creation_date() {
        when(clock.time()).thenReturn(LocalDateTime.parse("2020-01-20T13:41:21.007268"));

        var incident = incidentFactory
                .newIncident(new IncidentDTO("Bug", "BlaBlaBla", Priority.MINOR, new User()));
        assertThat(incident.getCreatedAt()).isEqualTo(LocalDateTime.parse("2020-01-20T13:41:21.007268"));
    }


}
