package com.alpha.userapp.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alpha.userapp.model.User;
import com.alpha.userapp.repositories.UserRepositories;

@Service
public class UserService {
	@Autowired
	private UserRepositories repositories;

	public UserDetailsService userDetailsService() {

		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return repositories.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
			}
		};
	}

	public User saveUser(User user) {
		user.setUpdatedAt(LocalDateTime.now());
		return repositories.save(user);
	}

}
