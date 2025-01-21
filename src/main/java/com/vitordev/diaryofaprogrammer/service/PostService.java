package com.vitordev.diaryofaprogrammer.service;

import com.vitordev.diaryofaprogrammer.domain.post.Post;
import com.vitordev.diaryofaprogrammer.domain.user.User;
import com.vitordev.diaryofaprogrammer.dto.AuthorDTO;
import com.vitordev.diaryofaprogrammer.dto.CommentDTO;
import com.vitordev.diaryofaprogrammer.repository.PostRepository;
import com.vitordev.diaryofaprogrammer.service.exceptions.AccessDeniedException;
import com.vitordev.diaryofaprogrammer.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Post post = postRepository.findById(postId).orElseThrow(() -> new ObjectNotFoundException("Post not found"));
        try {
            post.getComments().add(comment);
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Comment not found");
        }
        postRepository.save(post);
    }

    public void deleteComment(String postId, CommentDTO commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ObjectNotFoundException("Post not found"));
        post.getComments().remove(commentDto);
        postRepository.save(post);
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
                .map(x -> userService.findById(x))
                .map(AuthorDTO::new)
                .collect(Collectors.toList());
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public void update(Post oldPost, Post newPost) {
        oldPost.setTitle(newPost.getTitle() != null ? newPost.getTitle() : oldPost.getTitle());
        oldPost.setContent(newPost.getContent() != null ? newPost.getContent() : oldPost.getContent());
        oldPost.setCreatedAt(newPost.getCreatedAt() != null ? newPost.getCreatedAt() : oldPost.getCreatedAt());
        oldPost.setAuthor(newPost.getAuthor() != null ? newPost.getAuthor() : oldPost.getAuthor());
        oldPost.setComments(newPost.getComments() != null ? newPost.getComments() : oldPost.getComments());

        if (newPost.getLikedUsers() != null) {
            oldPost.getLikedUsers().addAll(newPost.getLikedUsers());
        }
    }

    public void validateOwnership(String postId, String username) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new ObjectNotFoundException("Post not found"));

        if (!post.getAuthor().getName().equals(username)) {
            throw new AccessDeniedException("You are not allowed to delete this post");
        }
    }
}

