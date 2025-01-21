package com.vitordev.diaryofaprogrammer.repository;

import com.vitordev.diaryofaprogrammer.domain.post.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByTitleContainingIgnoreCase(String name);
}
