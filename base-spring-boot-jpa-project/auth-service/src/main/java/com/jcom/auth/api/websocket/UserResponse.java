package com.jcom.auth.api.websocket;

public class UserResponse {

	private Long id;
	private Long timestamp;
	
	public UserResponse(Long id, long timestamp) {
		this.setId(id);
		this.setTimestamp(timestamp);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}