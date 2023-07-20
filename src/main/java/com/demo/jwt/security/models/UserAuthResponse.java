package com.demo.jwt.security.models;

public class UserAuthResponse {
	private final String JWT_TOKEN;

	public UserAuthResponse(String jWT_TOKEN) {
		super();
		JWT_TOKEN = jWT_TOKEN;
	}

	public String getJWT_TOKEN() {
		return JWT_TOKEN;
	}

}
