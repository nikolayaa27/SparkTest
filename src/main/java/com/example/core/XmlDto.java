package com.example.core;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
public class XmlDto {
    private String lastName;
    private String firstName;
    private String email;
    private String habbits;
}
