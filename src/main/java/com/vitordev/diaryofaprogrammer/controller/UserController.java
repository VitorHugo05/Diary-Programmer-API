package com.vitordev.diaryofaprogrammer.controller;

import com.vitordev.diaryofaprogrammer.domain.Post;
import com.vitordev.diaryofaprogrammer.domain.User;
import com.vitordev.diaryofaprogrammer.dto.PostDTO;
import com.vitordev.diaryofaprogrammer.dto.UserDTO;
import com.vitordev.diaryofaprogrammer.service.UserServices;
import com.vitordev.diaryofaprogrammer.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserServices userServices;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findUsers() {
        List<User> list = userServices.findAll();
        List<UserDTO> listDto = list.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String id) {
        User user = userServices.findById(id);
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

    @GetMapping(value = "/{id}/posts")
    public ResponseEntity<List<Post>> findPostsByUser(@PathVariable String id) {
        User user = userServices.findById(id);
        List<Post> posts = user.getPosts();
        if(posts.isEmpty()) {
            throw new ObjectNotFoundException("user without posts");
        }
        return ResponseEntity.ok().body(posts);
    }

    @PostMapping
    public ResponseEntity<Void> insertUser(@RequestBody User user) {
        user.setCreatedAt(new Date());
        user = userServices.insert(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userServices.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateData(@PathVariable String id, @RequestBody User user) {
        User oldUser = userServices.findById(id);
        UserDTO userDTO = userServices.updateData(oldUser, user);
        user = userServices.fromDTO(userDTO);
        user.setId(id);
        User newUser = userServices.update(user);
        return ResponseEntity.ok().body(new UserDTO(newUser));
    }
}
