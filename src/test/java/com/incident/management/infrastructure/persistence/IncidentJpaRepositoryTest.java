package com.incident.management.infrastructure.persistence;

import com.incident.management.domain.Incident;
import com.incident.management.domain.IncidentNumber;
import com.incident.management.domain.Priority;
import com.incident.management.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class IncidentJpaRepositoryTest {

    @Autowired
    private IncidentRepository incidentRepository;
    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @Test
    void should_injected_components_are_not_null() {
        assertThat(incidentRepository).isNotNull();
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
    }


    @Test
    void should_store_and_load_minimal_incident() {
        //given:
        var incident = newIncident();
        //when
        var persistIncident = incidentRepository.save(incident);
        //then:
        assertThat(persistIncident).isEqualToComparingFieldByField(incident);
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
