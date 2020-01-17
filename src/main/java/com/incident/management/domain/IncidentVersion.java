package com.incident.management.domain;

import com.google.common.base.Preconditions;
import lombok.Value;

@Value
public class IncidentVersion {
    private final String value;

    private IncidentVersion(String value) {
        this.value = value;
    }

    public static IncidentVersion of(String incidentName,String version){
        Preconditions.checkArgument(incidentName!=null);
        Preconditions.checkArgument(version!=null);
        return new IncidentVersion(String.format("%s-%s", incidentName, version));
    }

    @Override
    public String toString() {
        return getValue();
    }
}
