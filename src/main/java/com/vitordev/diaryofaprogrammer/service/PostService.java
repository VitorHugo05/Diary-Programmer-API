package com.vitordev.diaryofaprogrammer.service;

import com.vitordev.diaryofaprogrammer.domain.Post;
import com.vitordev.diaryofaprogrammer.domain.User;
import com.vitordev.diaryofaprogrammer.dto.AuthorDTO;
import com.vitordev.diaryofaprogrammer.dto.CommentDTO;
import com.vitordev.diaryofaprogrammer.dto.PostDTO;
import com.vitordev.diaryofaprogrammer.repository.PostRepository;
import com.vitordev.diaryofaprogrammer.service.exceptions.MissingFieldException;
import com.vitordev.diaryofaprogrammer.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserServices userService;

    public Post findById(String id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new ObjectNotFoundException("Post not found"));
    }

    public List<Post> findByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void delete(String id) {
        postRepository.deleteById(id);
    }

    public void addComment(String postId, CommentDTO comment) {
        Optional<Post> post = postRepository.findById(postId);
        post.get().getComments().add(comment);
        postRepository.save(post.get());
    }

    public Post likeOrDislikePost(String postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ObjectNotFoundException("Post not found"));
        User queryUser = userService.findById(userId);

        if (post.getLikedUsers().contains(queryUser.getUserId())) {
            post.getLikedUsers().remove(queryUser.getUserId());
        } else {
            post.getLikedUsers().add(queryUser.getUserId());
        }
        return postRepository.save(post);
    }

    public List<AuthorDTO> getLikedUsers(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ObjectNotFoundException("Post not found"));
        List<String> likedUserIds = post.getLikedUsers();

        return likedUserIds.stream()
                .map(userService::findById)
                .map(AuthorDTO::new)
                .collect(Collectors.toList());
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public Post fromDTO(PostDTO postDTO) {
        Post post = new Post();

        if (postDTO.getCreatedAt() == null) {
            post.setCreatedAt(new Date());
        } else {
            post.setCreatedAt(postDTO.getCreatedAt());
        }

        if (postDTO.getPostId() != null) {
            post.setPostId(postDTO.getPostId());
        }

        if (postDTO.getTitle() != null) {
            post.setTitle(postDTO.getTitle());
        }

        if (postDTO.getContent() != null) {
            post.setContent(postDTO.getContent());
        }

        if (postDTO.getAuthor() != null) {
            AuthorDTO authorDTO = postDTO.getAuthor();
            post.setAuthor(authorDTO);
        }

        post.setLikedUsers(new ArrayList<>());

        return post;
    }


    public Post updateData(Post oldPost, Post newPost) {
        if (newPost.getTitle() != null) {
            oldPost.setTitle(newPost.getTitle());
        }

        if (newPost.getContent() != null) {
            oldPost.setContent(newPost.getContent());
        }

        if (newPost.getCreatedAt() != null) {
            oldPost.setCreatedAt(newPost.getCreatedAt());
        }

        if (newPost.getAuthor() != null) {
            oldPost.setAuthor(newPost.getAuthor());
        }

        if (newPost.getComments() != null) {
            oldPost.setComments(newPost.getComments());
        }

        if (newPost.getLikedUsers() != null) {
             oldPost.getLikedUsers().addAll(newPost.getLikedUsers());
        }

        return oldPost;
    }

    public void validatePostFields(PostDTO postDTO) {
        if (postDTO == null) {
            throw new MissingFieldException("O objeto 'PostDTO' não pode ser nulo.");
        }

        if (postDTO.getTitle() == null || postDTO.getTitle().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'title' é obrigatório e não pode estar vazio.");
        }

        if (postDTO.getContent() == null || postDTO.getContent().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'content' é obrigatório e não pode estar vazio.");
        }

        if (postDTO.getCreatedAt() == null) {
            throw new MissingFieldException("O campo 'createdAt' é obrigatório.");
        }

        if (postDTO.getAuthor() == null) {
            throw new MissingFieldException("O campo 'author' é obrigatório.");
        }

        if (postDTO.getAuthor().getId() == null || postDTO.getAuthor().getId().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'author.postId' é obrigatório e não pode estar vazio.");
        }

        if (postDTO.getAuthor().getName() == null || postDTO.getAuthor().getName().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'author.name' é obrigatório e não pode estar vazio.");
        }
    }
}

