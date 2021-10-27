package com.jcom.auth.api.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity(name = "RolePermission")
@Table(name = "role_permission")
public class RolePermission implements Serializable {
 
    @EmbeddedId
    private RolePermissionId id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    private Role role;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    private Permission permission;
 
    private RolePermission() {}
 
    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
        this.id = new RolePermissionId(role.getId(), permission.getId());
    }
 
    public RolePermissionId getId() {
		return id;
	}

	public void setId(RolePermissionId id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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
 
        RolePermission that = (RolePermission) o;
        return Objects.equals(role, that.role) &&
               Objects.equals(permission, that.permission);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(role, permission);
    }
    
}