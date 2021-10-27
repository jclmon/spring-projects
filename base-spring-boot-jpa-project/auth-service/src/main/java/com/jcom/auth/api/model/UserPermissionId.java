package com.jcom.auth.api.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserPermissionId implements Serializable {
 
	private static final long serialVersionUID = 1L;

	@Column(name = "id_user")
    private Long userId;
 
    @Column(name = "id_permission")
    private String permissionId;
 
    private UserPermissionId() {}
 
    public UserPermissionId(Long userId, String permissionId) {
        this.userId = userId;
        this.permissionId = permissionId;
    }
    
    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        UserPermissionId that = (UserPermissionId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(permissionId, that.permissionId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(userId, permissionId);
    }
    
}