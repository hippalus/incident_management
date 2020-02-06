package com.incident.management.application;

import com.incident.management.IncidentManagementApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IncidentManagementApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class IncidentRelationshipControllerTest {
    @Autowired
    private IncidentRelationshipController incidentRelationshipController;
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void contextLoads() {
        assertThat(incidentRelationshipController).isNotNull();
    }

}
