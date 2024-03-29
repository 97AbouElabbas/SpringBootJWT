package com.demo.jwt.security.controllers;

import java.util.List;

import com.demo.jwt.dal.entities.User;
import com.demo.jwt.dal.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.jwt.security.models.UserAuthRequest;
import com.demo.jwt.security.models.UserAuthResponse;
import com.demo.jwt.security.services.AppUserDetailsService;
import com.demo.jwt.security.utils.JWTUtil;

@RestController
public class AuthenticationController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	AppUserDetailsService appUserDetailsService;
	
	@Autowired
    UserRepo userrepo;

	@RequestMapping(value = "/authentication", method = RequestMethod.POST)
	public ResponseEntity<?> authenticate(@RequestBody UserAuthRequest userAuthRequest) throws Exception {
		// 1 - make spring security authentication process
		try {
			List<User> users = userrepo.findByUsernameAndPassword(userAuthRequest.getUsername(), userAuthRequest.getPassword());
			if(users != null)
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthRequest.getUsername(),
					userAuthRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid Authentication", e);
		} catch (Exception e) {
			throw new Exception("Invalid Authentication", e);
		}
		// 2 - get authorized user details
		final UserDetails userDetails = appUserDetailsService.loadUserByUsername(userAuthRequest.getUsername());
		// 3 - generate token using JWTUtil.java
		final String JWT_TOKEN = jwtUtil.generateJWTToken(userDetails);
		// 4 - return JSON object contain the JWT token
		return ResponseEntity.ok(new UserAuthResponse(JWT_TOKEN));
	}
}
