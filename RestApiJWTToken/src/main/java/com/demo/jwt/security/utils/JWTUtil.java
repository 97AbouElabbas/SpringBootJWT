package com.demo.jwt.security.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWTUtil Class
 */
@Service
public class JWTUtil {
	/**
	 * JWT token Signature
	 */
	private String SECRET_SIGNATURE_KEY = "secret";
	/**
	 * in milliseconds
	 */
	private int TOKEN_LIFE_TIME = 1000 * 60 * 60 * 10;

	/**
	 * Extract username fro JWT token
	 * 
	 * @param token Extract
	 * @return
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extract expiration date
	 * 
	 * @param token Extract
	 * @return Date
	 */
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Extract payload data
	 * 
	 * @param <T>            Generic object
	 * @param token          JWT token
	 * @param claimsResolver
	 * @return
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Extract payload data
	 * 
	 * @param token JWT token
	 * @return Claims payload
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_SIGNATURE_KEY).parseClaimsJws(token).getBody();
	}

	/**
	 * Validate that the JWT token is not expired
	 * 
	 * @param token JWT token
	 * @return Boolean
	 */
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Generate JWT token method
	 * 
	 * @param userDetails   UserDetails which contains authorized user data
	 * @param tokenLifeTime JWT token Expiration in milliseconds
	 * @return String of JWT token
	 */
	public String generateJWTToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createJWTToken(claims, userDetails.getUsername(), TOKEN_LIFE_TIME);
	}

	/**
	 * Generate JWT token method
	 * 
	 * @param claims        claims the JWT payload
	 * @param subject       subject the JWT payload subject
	 * @param tokenLifeTime JWT token Expiration in milliseconds
	 * @return String of JWT token
	 */
	private String createJWTToken(Map<String, Object> claims, String subject, int tokenLifeTime) {
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims).setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + tokenLifeTime))
				.signWith(SignatureAlgorithm.HS256, SECRET_SIGNATURE_KEY);
		return builder.compact();
	}

	/**
	 * Validate that JWT token belongs to user & the token is not expired
	 * 
	 * @param token       JWT token
	 * @param userDetails UserDetails which contains authorized user data
	 * @return Boolean
	 */
	public Boolean validateJWTToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
