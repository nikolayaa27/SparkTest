package com.example.core;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class User {

    private String lastName;

    private String firstName;

    @Id
    private String email;

    private String habbits;

    private String password;

    User(XmlDto xml) {
        this.lastName = xml.getLastName();
        this.firstName = xml.getFirstName();
        this.email = xml.getEmail();
        this.habbits = xml.getHabbits();
        this.password = UUID.randomUUID().toString();
    }
}

