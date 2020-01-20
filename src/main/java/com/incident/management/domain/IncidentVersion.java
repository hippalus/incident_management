package com.incident.management.domain;

import com.google.common.base.Preconditions;
import lombok.Value;

import java.io.Serializable;

@Value
public class IncidentVersion implements Serializable {
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
