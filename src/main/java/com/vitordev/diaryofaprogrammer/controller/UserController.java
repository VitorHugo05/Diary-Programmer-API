package com.vitordev.diaryofaprogrammer.controller;

import com.vitordev.diaryofaprogrammer.domain.Post;
import com.vitordev.diaryofaprogrammer.domain.User;
import com.vitordev.diaryofaprogrammer.dto.PostDTO;
import com.vitordev.diaryofaprogrammer.dto.UserDTO;
import com.vitordev.diaryofaprogrammer.service.TokenService;
import com.vitordev.diaryofaprogrammer.service.UserServices;
import com.vitordev.diaryofaprogrammer.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findUsers() {
        List<User> list = userServices.findAll();
        if (list.isEmpty()) {
            throw new ObjectNotFoundException("User not found");
        }
        List<UserDTO> listDto = list.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String userId) {
        User user = userServices.findById(userId);
        if (user == null) {
            throw new ObjectNotFoundException("User not found");
        }
        return ResponseEntity.ok().body(new UserDTO(user));
    }

    @GetMapping(value = "/name")
    public ResponseEntity<List<UserDTO>> findUserByName(@RequestParam(value = "name", defaultValue = "") String name) {
        List<User> users = name != null ? userServices.findByName(name) : userServices.findAll();
        if(users.isEmpty()) {
            throw new ObjectNotFoundException("User not found");
        }
        List<UserDTO> listDto = users.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/{userId}/posts")
    public ResponseEntity<List<PostDTO>> findPostsByUser(@PathVariable String userId) {
        User user = userServices.findById(userId);
        List<Post> posts = user.getPosts();
        if(posts.isEmpty()) {
            throw new ObjectNotFoundException("user without posts");
        }
        List<PostDTO> postDTO = posts.stream().map(PostDTO::new).toList();
        return ResponseEntity.ok().body(postDTO);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userServices.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<UserDTO> updateData(@PathVariable String userId, @RequestBody UserDTO userDto) {
        userServices.validateUserDTOFields(userDto);

        User oldUser = userServices.findById(userId);
        if(oldUser == null) {
            throw new ObjectNotFoundException("User not found");
        }

        userDto.setUserId(oldUser.getUserId());
        User newUser = userServices.fromDTO(userDto);
        userServices.updateData(oldUser, newUser);
        User user = userServices.save(newUser);

        return ResponseEntity.ok().body(new UserDTO(user));
    }
}
