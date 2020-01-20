package com.incident.management.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class User implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;

    public User(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
    }
}
