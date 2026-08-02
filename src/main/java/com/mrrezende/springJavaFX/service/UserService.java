package com.mrrezende.springJavaFX.service;

import com.mrrezende.springJavaFX.model.User;
import com.mrrezende.springJavaFX.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name) {
        return userRepository.save(new User(name));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado: id=" + id));
    }
}
