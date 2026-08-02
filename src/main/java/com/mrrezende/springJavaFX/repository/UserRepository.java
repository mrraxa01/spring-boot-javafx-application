package com.mrrezende.springJavaFX.repository;

import com.mrrezende.springJavaFX.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
