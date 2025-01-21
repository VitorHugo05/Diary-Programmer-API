package com.vitordev.diaryofaprogrammer.domain.post;

import com.vitordev.diaryofaprogrammer.dto.CommentDTO;
import com.vitordev.diaryofaprogrammer.dto.PostDTO;
import com.vitordev.diaryofaprogrammer.service.exceptions.MissingFieldException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PostUtils {

    public Post fromDTO(PostDTO postDTO) {
        Post post = new Post();
        post.setPostId(postDTO.getPostId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCreatedAt(postDTO.getCreatedAt() == null ? new Date() : postDTO.getCreatedAt());
        post.setAuthor(postDTO.getAuthor());
        return post;
    }

    public PostDTO toDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getPostId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setAuthor(post.getAuthor());
        postDTO.setLikes(post.getLikedUsers().size());
        postDTO.setComments(post.getComments());
        return postDTO;
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

    public void validateCommentDTOFields(CommentDTO comment) {
        if (comment.getCommentId() == null || comment.getCommentId().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'commentId' é obrigatório e não pode estar vazio.");
        }

        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'content' é obrigatório e não pode estar vazio.");
        }

        if (comment.getAuthor() == null) {
            throw new MissingFieldException("O campo 'author' é obrigatório e não pode ser nulo.");
        }
    }
}