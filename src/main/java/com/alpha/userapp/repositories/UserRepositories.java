package com.alpha.userapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.userapp.model.User;

public interface UserRepositories extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
