package com.demo.jwt.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.jwt.security.services.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.demo.jwt.security.utils.JWTUtil;

@Component
public class ApiAuthFilter extends OncePerRequestFilter {
	@Autowired
    AppUserDetailsService appUserDetailService;
	@Autowired
	JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// get Authentication attribute from http header
		final String AUTHENTICATION = request.getHeader("Authorization");
		// check authentication contain jwt token
		if (AUTHENTICATION != null && AUTHENTICATION.startsWith("Bearer ")) {
			String jwt = AUTHENTICATION.substring(7);
			String username = jwtUtil.extractUsername(jwt);
			// hold security context and do validation programmatically
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = appUserDetailService.loadUserByUsername(username);
				if (jwtUtil.validateJWTToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}
		// continue chain
		filterChain.doFilter(request, response);
	}
}
