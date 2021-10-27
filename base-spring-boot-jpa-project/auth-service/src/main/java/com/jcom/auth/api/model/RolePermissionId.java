package com.jcom.auth.api.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RolePermissionId implements Serializable {
 
	private static final long serialVersionUID = 1L;

	@Column(name = "id_role")
    private String roleId;
 
    @Column(name = "id_permission")
    private String permissionId;
 
    private RolePermissionId() {}
 
    public RolePermissionId(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
    
    public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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
 
        RolePermissionId that = (RolePermissionId) o;
        return Objects.equals(roleId, that.roleId) &&
               Objects.equals(permissionId, that.permissionId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }
    
}