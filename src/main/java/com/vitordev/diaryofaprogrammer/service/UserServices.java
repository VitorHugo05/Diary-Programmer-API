package com.vitordev.diaryofaprogrammer.service;

import com.vitordev.diaryofaprogrammer.domain.Post;
import com.vitordev.diaryofaprogrammer.domain.User;
import com.vitordev.diaryofaprogrammer.dto.UserDTO;
import com.vitordev.diaryofaprogrammer.repository.UserRepository;
import com.vitordev.diaryofaprogrammer.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        Optional<User> query = userRepository.findById(id);
        return query.orElseThrow(() -> new ObjectNotFoundException("User not found"));
    }

    public List<User> findByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public User insert(User user) {
        return userRepository.save(user);
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public UserDTO updateData(User oldUser, User user) {
        UserDTO userDTO = new UserDTO();

        if (user.getName() != null && !user.getName().equals(oldUser.getName())) {
            userDTO.setName(user.getName());
        } else {
            userDTO.setName(oldUser.getName());
        }

        if (user.getEmail() != null && !user.getEmail().equals(oldUser.getEmail())) {
            userDTO.setEmail(user.getEmail());
        } else {
            userDTO.setEmail(oldUser.getEmail());
        }

        if (user.getBirthdate() != null && !user.getBirthdate().equals(oldUser.getBirthdate())) {
            userDTO.setBirthdate(user.getBirthdate());
        } else {
            userDTO.setBirthdate(oldUser.getBirthdate());
        }

        if (user.getLikes() != null && !user.getLikes().equals(oldUser.getLikes())) {
            userDTO.setLikes(user.getLikes());
        } else {
            userDTO.setLikes(oldUser.getLikes());
        }

        userDTO.setId(oldUser.getId());
        userDTO.setCreatedAt(oldUser.getCreatedAt());
        userDTO.setPosts(oldUser.getPosts().stream().map(Post::getId).collect(Collectors.toList()));
        return userDTO;
    }

    public User fromDTO(UserDTO userDTO) {
        User user = new User();
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        user.setPassword(user.getPassword());

        if (userDTO.getBirthdate() != null) {
            user.setBirthdate(userDTO.getBirthdate());
        }

        if (userDTO.getLikes() != null) {
            user.setLikes(userDTO.getLikes());
        }

        user.setCreatedAt(userDTO.getCreatedAt());
        user.setId(userDTO.getId());
        user.setPosts(userDTO.getPosts().stream().map(postId -> {
            Post post = new Post();
            post.setId(postId);
            return post;
        }).collect(Collectors.toList()));

        return user;
    }

}
