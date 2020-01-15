package com.incident.management.domain;

import com.incident.management.domain.exceptions.PropertyRequiredException;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class IncidentTest {

    @Test
    void create_incident() {
        var incident = newIncident();
        assertNotNull(incident);

    }

    @Test
    void create_incident_with_attachment() {
        try {
            Blob blob = new SerialBlob(new byte[]{1, 2, 3});
            var incident = Incident.builder()
                    .withId(1L)
                    .withTitle("aslkal")
                    .withPriority(Priority.BLOCKER)
                    .withCreatedBy(new User(1L, "Habip", "ISler"))
                    .withDescription("Calismisyor")
                    .withContent(blob)
                    .build();
            assertNotNull(incident);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Test
    void throws_exceptions_when_requeried_properties_missing() {
        assertThrows(PropertyRequiredException.class, () -> Incident.builder()
                .withId(1L)
                .withTitle("aslkal")
                .withPriority(Priority.BLOCKER)
                .build());

    }

    @Test
    void initial_state_must_be_open() {

        var incident = newIncident();

        assertEquals(IncidentStatus.OPEN, incident.getStatus());

    }

    @Test
    void should_be_in_assigned_state_after_assigning_participant() {
        // given:
        var incident = newIncident();

        // when:
        incident.assignTo(AssigneeID.of("murtaza"));

        // then:
        assertThat(incident.getStatus()).isEqualTo(IncidentStatus.ASSIGNED);
    }

    @Test
    void should_fail_meaning_fully_if_Assigning_invalid_participant() {

        // given:
        var incident = newIncident();

        // expect when
        assertThrows(IllegalArgumentException.class, () -> incident.assignTo(null));
    }

    @Test
    void should_have_assignee_after_assigning_participant() {

        // given:
        var ticket = newIncident();

        // when:
        ticket.assignTo(AssigneeID.of("homer.simpson"));

        // then:
        assertThat(ticket.getAssigneeID()).isEqualTo(AssigneeID.of("homer.simpson"));
    }
    @Test
     void shouldBeInResolvedStateAfterFixing()  {

        // given:
        var ticket = newIncident();
        // when:
        ticket.fixedIn(IncidentVersion.of("bug","version-1.1.2"));

        // then:
        assertThat(ticket.getStatus()).isEqualTo(IncidentStatus.RESOLVED);
    }
    @Test
     void should_fail_meaning_fully_if_assigning_invalid_fix_version() {

        // given:
        var incident = newIncident();

        // expect when:
        assertThrows(IllegalArgumentException.class, () -> incident.fixedIn(null));
    }
    @Test
     void should_have_resolution_after_fixing()  {

        // given:
        var incident = newIncident();
        // when:
        incident.fixedIn(IncidentVersion.of("bug","version-1.1.2"));

        // then:
        assertThat(incident.getResolution()).isEqualTo(Resolution.FIXED);
    }
    @Test
    void should_have_fix_version_after_fixing() {

        // given:
        Incident incident = newIncident();

        // when:
        incident.fixedIn(IncidentVersion.of("bug","version-1.1.2"));

        // then:
        assertThat(incident.getFixVersion()).isEqualTo(IncidentVersion.of("bug","version-1.1.2"));
    }

    private Incident newIncident() {
        return Incident.builder()
                .withId(1L)
                .withTitle("aslkal")
                .withPriority(Priority.MAJOR)
                .withCreatedBy(new User(1L, "Habip", "ISler"))
                .withDescription("Calismisyor")
                .build();
    }
}
