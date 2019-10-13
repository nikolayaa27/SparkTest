package com.example.core;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Error {


    private String error;
    private Date date;

    @Id
    private String id;


    Error(String error) {
        this.error = error;
        this.id = UUID.randomUUID().toString();
        this.date = new Date();


    }
}

