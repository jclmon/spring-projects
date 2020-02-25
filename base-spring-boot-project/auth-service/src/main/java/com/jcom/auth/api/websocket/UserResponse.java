package com.jcom.auth.api.websocket;

public class UserResponse {

	private String id;
	private Long timestamp;
	
	public UserResponse(String id, long timestamp) {
		this.setId(id);
		this.setTimestamp(timestamp);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	
}