package com.example.testcontainers.users;

import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
  @Query("{'name': {$regex : ?0, $options: 'i'}}")
  Stream<User> findByNameRegex(String pattern);
}
