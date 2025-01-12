package com.vitordev.diaryofaprogrammer.repository;

import com.vitordev.diaryofaprogrammer.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByNameContainingIgnoreCase(String name);
}
