package com.example.core;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DataRepository extends CrudRepository<User, Integer> {

    public Optional<User> findByEmailAndPassword(String email, String password);

}








