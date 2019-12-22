package com.incident.management.domain;

import com.incident.management.domain.exceptions.PropertyRequiredException;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class IncidentTest {

   @Test
    void create_incident(){
        var incident=Incident.builder()
                .withId(1L)
                .withTitle("aslkal")
                .withCategory(new Category(1L,"Critical",Duration.ofHours(1)))
                .withCreatedBy(new User(1L,"Habip","ISler"))
                .withDescription("Calismisyor")
                .build();
        assertNotNull(incident);

    }

    @Test
    void create_incident_with_attachment(){
        try {
            Blob blob=new SerialBlob(new byte[]{1,2,3});
            var incident=Incident.builder()
                    .withId(1L)
                    .withTitle("aslkal")
                    .withCategory(new Category(1L,"Critical",Duration.ofHours(1)))
                    .withCreatedBy(new User(1L,"Habip","ISler"))
                    .withDescription("Calismisyor")
                    .withContent(blob)
                    .build();
            assertNotNull(incident);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
  @Test
    void throws_exceptions_when_requeried_properties_missing(){
      assertThrows(PropertyRequiredException.class,()->Incident.builder()
              .withId(1L)
              .withTitle("aslkal")
              .withCategory(new Category(1L,"Critical",Duration.ofHours(1)))
              .build());


    }
    @Test
    void initial_state_must_be_open(){

        var incident=Incident.builder()
                .withId(1L)
                .withTitle("aslkal")
                .withCategory(new Category(1L,"Critical",Duration.ofHours(1)))
                .withCreatedBy(new User(1L,"Habip","ISler"))
                .withDescription("Calismisyor")
                .build();
        assertEquals(IncidentStatus.OPEN,incident.getStatus());

    }
    @Test
    void change_state_open_to_in_progress(){

        var incident=Incident.builder()
                .withId(1L)
                .withTitle("aslkal")
                .withCategory(new Category(1L,"Critical",Duration.ofHours(1)))
                .withCreatedBy(new User(1L,"Habip","ISler"))
                .withDescription("Calismisyor")
                .withStatus(IncidentStatus.IN_PROGRESS)
                .build();
        assertEquals(IncidentStatus.IN_PROGRESS,incident.getStatus());

    }





}
