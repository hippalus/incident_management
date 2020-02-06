package com.incident.management.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class IncidentDTO {
    private  String title;
    private  String description;
    private  Priority priority;
    private  User createdBy;


}
