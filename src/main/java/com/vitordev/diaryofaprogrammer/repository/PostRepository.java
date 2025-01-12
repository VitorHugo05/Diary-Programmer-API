package com.vitordev.diaryofaprogrammer.repository;

import com.vitordev.diaryofaprogrammer.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {

}
