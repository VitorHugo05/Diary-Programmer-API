package com.vitordev.diaryofaprogrammer.service;

import com.vitordev.diaryofaprogrammer.domain.Post;
import com.vitordev.diaryofaprogrammer.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post findById(String id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElse(null);
    }

    public List<Post> findByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
