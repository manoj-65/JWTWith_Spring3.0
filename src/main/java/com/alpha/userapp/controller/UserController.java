package com.alpha.userapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.userapp.dto.JwtAuthenticationResponse;
import com.alpha.userapp.dto.SignInRequest;
import com.alpha.userapp.dto.SignUpRequest;
import com.alpha.userapp.services.AuthenticationService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/signup")
	public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
		return authenticationService.signup(request);
	}

	@PostMapping("/signing")
	public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
		return authenticationService.signin(request);
	}

	@GetMapping("/validate/anon")
	public String anonEndPoint() {
		System.out.println("Hi");
		return "everyone can see this";
	}

	@GetMapping("/validate/users")
	@PreAuthorize("hasRole('USER')")
	public String usersEndPoint() {
		System.out.println("Hi");
		return "ONLY users can see this";
	}

	@GetMapping("/validate/admins")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminsEndPoint() {
		return "ONLY admins can see this";
	}
}
