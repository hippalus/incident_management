package com.incident.management.domain;

import com.incident.management.domain.Incident.RelatedIncident;
import com.incident.management.domain.exceptions.PropertyRequiredException;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;

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
            var blob = new SerialBlob(new byte[]{1, 2, 3});
            var incident = Incident.builder()
                    .withId(IncidentNumber.of("1"))
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
    void throws_exceptions_when_required_properties_missing() {
        assertThrows(PropertyRequiredException.class, () -> Incident.builder()
                .withId(IncidentNumber.of("1"))
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
        var incident = newIncident();

        // when:
        incident.assignTo(AssigneeID.of("homer.simpson"));

        // then:
        assertThat(incident.getAssignee()).isEqualTo(AssigneeID.of("homer.simpson"));
    }

    @Test
    void should_be_in_resolved_state_after_fixing() {
        // given:
        var incident = newIncident();
        // when:
        incident.fixedIn(IncidentVersion.of("bug", "version-1.1.2"));
        // then:
        assertThat(incident.getStatus()).isEqualTo(IncidentStatus.RESOLVED);
    }

    @Test
    void should_fail_meaning_fully_if_assigning_invalid_fix_version() {

        // given:
        var incident = newIncident();

        // expect when:
        assertThrows(IllegalArgumentException.class, () -> incident.fixedIn(null));
    }

    @Test
    void should_have_resolution_after_fixing() {

        // given:
        var incident = newIncident();
        // when:
        incident.fixedIn(IncidentVersion.of("bug", "version-1.1.2"));

        // then:
        assertThat(incident.getResolution()).isEqualTo(Resolution.FIXED);
    }

    @Test
    void should_have_fix_version_after_fixing() {

        // given:
        var incident = newIncident();

        // when:
        incident.fixedIn(IncidentVersion.of("bug", "version-1.1.2"));

        // then:
        assertThat(incident.getFixVersion()).isEqualTo(IncidentVersion.of("bug", "version-1.1.2"));
    }

    @Test
    void should_be_in_resolved_state_after_marking_as_duplicate() {

        // given:
        var incident = newIncident();

        // when:
        incident.duplicateOf(IncidentNumber.of("152"));

        // then:
        assertThat(incident.getStatus()).isEqualTo(IncidentStatus.RESOLVED);
        assertThat(incident.getResolution()).isEqualTo(Resolution.DUPLICATE);
    }

    @Test
    void should_fail_meaning_fully_if_marking_as_duplicate_of_invalid_incident() {
        // given:
        var incident = newIncident();

        // when  expect:
        assertThrows(IllegalArgumentException.class, () -> incident.duplicateOf(null));
    }

    @Test
    void should_have_related_incident_after_marking_as_duplicate() {

        // given:
        var incident = newIncident();

        // when:
        incident.duplicateOf(IncidentNumber.of("986"));

        // then:
        assertThat(incident.isDuplicateOf(IncidentNumber.of("986"))).isTrue();
    }

    @Test
    void should_be_in_resolved_state_after_marking_as_cannot_reproduce() {

        // given:
        var incident = newIncident();

        // when:
        incident.cannotReproduce();

        // then:
        assertThat(incident.getStatus()).isEqualTo(IncidentStatus.RESOLVED);
    }

    @Test
    void should_have_resolution_after_marking_as_cannot_reproduce() {

        // given:
        var incident = newIncident();

        // when:
        incident.cannotReproduce();

        // then:
        assertThat(incident.getResolution()).isEqualTo(Resolution.CANNOT_REPRODUCE);
    }

    @Test
    void should_be_in_resolved_state_after_marking_as_wont_fix() {

        // given:
        var incident = newIncident();

        // when:
        incident.wontFix("Works this way by design.");

        // then:
        assertThat(incident.getStatus()).isEqualTo(IncidentStatus.RESOLVED);
    }

    @Test
    void should_fail_meaning_fully_if_providing_invalid_explanation() {

        // given:
        var incident = newIncident();

        // when then expect
        assertThrows(IllegalArgumentException.class, () -> incident.wontFix(null));
    }

    @Test
    void should_have_resolution_after_marking_as_wont_fix() {

        // given:
        var incident = newIncident();

        // when:
        incident.wontFix("Works this way by design.");

        // then:
        assertThat(incident.getResolution()).isEqualTo(Resolution.WONT_FIX);
    }

    @Test
    void should_be_in_closed_state_after_closing() {

        // given:
        var incident = resolvedIncident();

        // when:
        incident.close();

        // then:
        assertThat(incident.getStatus()).isEqualTo(IncidentStatus.CLOSED);
    }

    @Test
    void should_go_back_to_open_state_after_reopening() {

        // given:
        var incident = closedIncident();

        // when:
        incident.reopen(IncidentVersion.of("asd", "1.1.4"));

        // then:
        assertThat(incident.getStatus()).isEqualTo(IncidentStatus.OPEN);
    }

    @Test
    void should_fail_meaning_fully_if_assigning_invalid_fix_version_on_reopen() {

        // given:
        var incident = closedIncident();
        // when then expect
        assertThrows(IllegalArgumentException.class, () -> incident.reopen(null));
    }

    @Test
    void should_reset_occurred_in_after_reopening() {

        // given:
        var incident = closedIncident();

        // when:
        incident.reopen(IncidentVersion.of("bug-prod", "1.1.4"));

        // then:
        assertThat(incident.getOccurredIn()).isEqualTo(IncidentVersion.of("bug-prod", "1.1.4"));
    }

    @Test
    void should_reset_assignee_after_reopening() {
        // given:
        var incident = resolvedIncident();
        incident.assignTo(AssigneeID.of("Josef"));
        // when:
        incident.reopen(IncidentVersion.of("bug-prod", "1.1.4"));

        // then:
        assertNull(incident.getAssignee());
    }

    @Test
    void should_have_related_incident_after_marking_as_blocker() {
        // given:
        var incident = newIncident();

        // when:
        incident.blockedBy(IncidentNumber.of("154"));

        // then:
        assertTrue(incident.hasRelationshipTo(IncidentNumber.of("154"), RelatedIncident.RelationshipType.BLOCKED_BY));
    }

    @Test
    void should_have_related_incident_after_referring_to_other_incident() {
        // given:
        var incident = newIncident();

        // when:
        incident.relatedTo(IncidentNumber.of("512"));

        // then:
        assertTrue(incident.hasRelationshipTo(IncidentNumber.of("512"), RelatedIncident.RelationshipType.RELATED_TO));
    }

    @Test
    void should_fail_meaning_fully_if_referring_to_invalid_incident() {
        // given:
        var incident = newIncident();
        String a="habip";
        a="ss";
        System.out.println(a);
        // when expect:
        assertThrows(IllegalArgumentException.class, () -> incident.relatedTo(null));

    }

    @Test
    void should_fail_meaning_fully_if_blocks_invalid_incident() {
        // given:
        var incident = newIncident();
        // when expect:
        assertThrows(IllegalArgumentException.class, () -> incident.blockedBy(null));

    }


    private Incident closedIncident() {
        var incident = resolvedIncident();
        incident.close();
        return incident;
    }


    private Incident resolvedIncident() {
        var incident = newIncident();
        incident.fixedIn(IncidentVersion.of("buggy", "3.2.1"));
        return incident;
    }

    private Incident newIncident() {
        return Incident.builder()
                .withId(IncidentNumber.of("1"))
                .withTitle("aslkal")
                .withPriority(Priority.MAJOR)
                .withCreatedBy(new User(1L, "Habip", "ISler"))
                .withDescription("Calismisyor")
                .build();
    }
}
