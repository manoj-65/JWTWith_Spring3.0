package com.alpha.userapp.filters;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alpha.userapp.services.JwtService;
import com.alpha.userapp.services.UserService;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticayionFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserService service;

	@Override
	protected void doFilterInternal(@Nonnull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		log.debug("JWT-{}", jwt.toString());
		userEmail = jwtService.extractUserName(jwt);
		if (!(StringUtils.isEmpty(userEmail)) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails details = service.userDetailsService().loadUserByUsername(userEmail);
			if (jwtService.isTokenValid(jwt, details)) {
				log.debug("User-{}", details);
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details, null,
						details.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				context.setAuthentication(authToken);
				SecurityContextHolder.setContext(context);
			}

		}
		filterChain.doFilter(request, response);
	}

}
