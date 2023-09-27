package com.alpha.userapp.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class JwtService {
	@Value("${token.secret.key}")
	private String jwtSecretKey;

	@Value("${token.expirationms}")
	private Long jwtExpirationMs;

	public String extractUserName(String jwt) {
		return extractClaim(jwt, Claims::getSubject);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public boolean isTokenValid(String jwt, UserDetails details) {
		String userName = extractUserName(jwt);
		return (userName.equals(details.getUsername())) && isTokenExpired(jwt);
	}

	private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolvers) {
		final Claims claims = extractAllClaims(jwt);
		return claimsResolvers.apply(claims);
	}

	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	private boolean isTokenExpired(String jwt) {
		return extractExpiration(jwt).after(new Date());
	}

	private Date extractExpiration(String jwt) {
		return extractClaim(jwt, Claims::getExpiration);
	}

	private Claims extractAllClaims(String jwt) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody();
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	

}
