package com.jcom.auth.api.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserRoleId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id_user")
    private Long userId;
 
    @Column(name = "id_role")
    private String roleId;
 
    private UserRoleId() {}
 
    public UserRoleId(Long userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
    
    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(roleId, that.roleId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }

}