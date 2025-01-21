package com.vitordev.diaryofaprogrammer.domain.user;

import com.vitordev.diaryofaprogrammer.domain.post.Post;
import com.vitordev.diaryofaprogrammer.dto.RequestLoginDTO;
import com.vitordev.diaryofaprogrammer.dto.UserDTO;
import com.vitordev.diaryofaprogrammer.service.exceptions.MissingFieldException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserUtils {

    public User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setBirthdate(userDTO.getBirthdate());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setPosts(userDTO.getPosts().stream().map(postId -> {
            Post post = new Post();
            post.setPostId(postId);
            return post;
        }).collect(Collectors.toList()));
        return user;
    }

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setBirthdate(user.getBirthdate());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setPosts(user.getPosts().stream().map(Post::getPostId).collect(Collectors.toList()));
        return userDTO;
    }

    public void validateRegisterFields(User user) {
        if (user == null) {
            throw new MissingFieldException("O objeto não pode ser nulo.");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'name' é obrigatório e não pode estar vazio.");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'email' é obrigatório e não pode estar vazio.");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'password' é obrigatório e não pode estar vazio.");
        }

        if (user.getBirthdate() == null) {
            throw new MissingFieldException("O campo 'birthdate' é obrigatório.");
        }
    }

    public void validateLoginFields(RequestLoginDTO user) {
        if (user == null) {
            throw new MissingFieldException("O objeto não pode ser nulo.");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'email' é obrigatório e não pode estar vazio.");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'password' é obrigatório e não pode estar vazio.");
        }
    }

    public void validateUserDTOFields(UserDTO user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'name' é obrigatório e não pode estar vazio.");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new MissingFieldException("O campo 'email' é obrigatório e não pode estar vazio.");
        }

        if (user.getCreatedAt() == null) {
            throw new MissingFieldException("O campo 'createdAt' é obrigatório.");
        }

        if (user.getBirthdate() == null) {
            throw new MissingFieldException("O campo 'birthdate' é obrigatório.");
        }

        if (user.getPosts() == null) {
            throw new MissingFieldException("A lista 'posts' é obrigatória e não pode ser nula.");
        }
    }
}
