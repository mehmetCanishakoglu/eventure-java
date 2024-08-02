package com.example.eventurejavabackend.repository;

import com.example.eventurejavabackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}