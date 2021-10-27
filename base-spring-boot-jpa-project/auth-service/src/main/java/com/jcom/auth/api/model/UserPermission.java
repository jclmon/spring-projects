package com.jcom.auth.api.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity(name = "UserPermission")
@Table(name = "user_permission")
public class UserPermission implements Serializable {
 
    @EmbeddedId
    private UserPermissionId id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    private Permission permission;
 
    private UserPermission() {}
 
    public UserPermission(User user, Permission permission) {
        this.user = user;
        this.permission = permission;
        this.id = new UserPermissionId(user.getId(), permission.getId());
    }
 
    public UserPermissionId getId() {
		return id;
	}

	public void setId(UserPermissionId id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        UserPermission that = (UserPermission) o;
        return Objects.equals(user, that.user) &&
               Objects.equals(permission, that.permission);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(user, permission);
    }
    
}