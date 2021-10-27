package com.jcom.auth.api.security.dto;

public class JwtResponse {

	private String token;
	private String userId;
	private String expiration;

	public JwtResponse() {
	}

	public JwtResponse(String token, String userId, String expiration) {
		this.token = token;
		this.userId = userId;
		this.expiration = expiration;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

}
