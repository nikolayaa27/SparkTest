package com.example.core;

import org.springframework.data.repository.CrudRepository;

public interface DataRepository extends CrudRepository<User, Integer> {
}
