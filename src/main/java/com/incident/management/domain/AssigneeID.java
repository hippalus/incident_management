package com.incident.management.domain;

import lombok.Value;

import java.io.Serializable;

@Value
public class AssigneeID implements Serializable {
    private final String id;

    private AssigneeID(String id) {
        this.id = id;
    }

    public static AssigneeID of(String id) {
        return new AssigneeID(id);
    }

    @Override
    public String toString() {
        return getId();
    }
}
