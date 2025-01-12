package com.vitordev.diaryofaprogrammer.repository;

import com.vitordev.diaryofaprogrammer.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByNameContainingIgnoreCase(String name);
    Optional<User> findByEmailIgnoreCase(String email);
}
