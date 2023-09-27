package com.alpha.userapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.alpha.userapp.model.User;
import com.alpha.userapp.repositories.UserRepositories;
import com.alpha.userapp.services.UserService;
import com.alpha.userapp.util.Role;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SeedDataConfig implements CommandLineRunner {
	@Autowired
	private UserRepositories userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;

	@Override
	public void run(String... args) throws Exception {
		if (userRepository.count() == 0) {

			User admin = User.builder().firstName("admin").lastName("admin").email("admin@admin.com")
					.password(passwordEncoder.encode("password")).role(Role.ADMIN).build();

			userService.saveUser(admin);
			log.debug("created ADMIN user - {}", admin);
		}

	}
}
