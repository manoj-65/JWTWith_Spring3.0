package com.alpha.userapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alpha.userapp.dto.JwtAuthenticationResponse;
import com.alpha.userapp.dto.SignInRequest;
import com.alpha.userapp.dto.SignUpRequest;
import com.alpha.userapp.model.User;
import com.alpha.userapp.repositories.UserRepositories;
import com.alpha.userapp.util.Role;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepositories repositories;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;

	public JwtAuthenticationResponse signup(SignUpRequest request) {
		var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
				.email(request.getEmail()).password(encoder.encode(request.getPassword())).role(Role.USER).build();

		user = userService.saveUser((User) user);
		var jwt = jwtService.generateToken((UserDetails) user);
		return JwtAuthenticationResponse.builder().jwt(jwt).build();
	}

	public JwtAuthenticationResponse signin(SignInRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = repositories.findByEmail(request.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
		var jwt = jwtService.generateToken((UserDetails) user);
		return JwtAuthenticationResponse.builder().jwt(jwt).build();
	}

}
