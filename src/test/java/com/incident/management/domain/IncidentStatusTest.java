package com.incident.management.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class IncidentStatusTest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class MockIncident {
        public IncidentStatus status = IncidentStatus.OPEN;
    }

    //should be not transfer from ON to OFF
    @Test
    void should_be_not_transit_from_ON_to_OFF() {
        //given:
        var incident = new MockIncident();
        //expect when :
        assertThatIllegalStateException()
                .isThrownBy(() -> incident.status = incident.status.close())
                .withMessage("Cannot transit from OPEN to CLOSED!");

    }

    @Test
    void should_be_not_transit_from_ON_to_REOPEN() {
        //given:
        var incident = new MockIncident();
        //expect when :
        assertThatIllegalStateException()
                .isThrownBy(() -> incident.status = incident.status.reopen())
                .withMessage("Cannot transit from OPEN to OPEN!");
    }

    @Test
    void should_be_not_transit_from_ASSIGNED_to_CLOSE() {
        //given:
        var incident = new MockIncident(IncidentStatus.ASSIGNED);
        //expect when :
        assertThatIllegalStateException()
                .isThrownBy(() -> incident.status = incident.status.close())
                .withMessage("Cannot transit from ASSIGNED to CLOSED!");

    }

    @Test
    void should_be_not_transit_from_ON_CLOSED_to_ASSIGNED() {
        //given:
        var incident = new MockIncident(IncidentStatus.CLOSED);
        //expect when :
        assertThatIllegalStateException()
                .isThrownBy(() -> incident.status = incident.status.assign())
                .withMessage("Cannot transit from CLOSED to ASSIGNED!");
    }

    @Test
    void should_be_not_transit_from_ON_CLOSED_to_RESOLVED() {
        //given:
        var incident = new MockIncident(IncidentStatus.CLOSED);
        //expect when :
        assertThatIllegalStateException()
                .isThrownBy(() -> incident.status = incident.status.resolve())
                .withMessage("Cannot transit from CLOSED to RESOLVED!");

    }
}