package com.vitordev.diaryofaprogrammer.service;

import com.vitordev.diaryofaprogrammer.domain.user.User;
import com.vitordev.diaryofaprogrammer.repository.UserRepository;
import com.vitordev.diaryofaprogrammer.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public void update(User existingUser, User updatedUser) {
        existingUser.setName(updatedUser.getName() != null ? updatedUser.getName() : existingUser.getName());
        existingUser.setEmail(updatedUser.getEmail() != null ? updatedUser.getEmail() : existingUser.getEmail());
        existingUser.setBirthdate(updatedUser.getBirthdate() != null ? updatedUser.getBirthdate() : existingUser.getBirthdate());
    }
}


