package com.jcom.auth.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission implements Serializable, com.ms.core.common.security.Permission {

	@Id
    private String id;
	
	@Column
    private String code;

    @Column
    private Integer status = 1; //default status

    @ManyToMany(mappedBy = "permissionsRoles")
    private List<Role> permissionRoles = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "permissions")
    private List<User> permissionUsers = new ArrayList<>();
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Role> getPermissionRoles() {
		return permissionRoles;
	}

	public void setPermissionRoles(List<Role> permissionRoles) {
		this.permissionRoles = permissionRoles;
	}

	public List<User> getPermissionUsers() {
		return permissionUsers;
	}

	public void setPermissionUsers(List<User> permissionUsers) {
		this.permissionUsers = permissionUsers;
	}

	
}
