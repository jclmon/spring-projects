package com.jcom.auth.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "role")
public class Role implements Serializable, com.ms.core.common.security.Role {
	
    @Id
    private String id;
    
    @Column
    private Integer status = 1; //default status

    @Column
    private String code;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "role_permission",
            joinColumns = @JoinColumn(name = "id_role"),
            inverseJoinColumns = @JoinColumn(name = "id_permission")
    )
    private List<Permission> permissionsRoles = new ArrayList<>();

    @ManyToMany(mappedBy = "roles")
    private List<User> roleUsers = new ArrayList<>();
   
	public Role() {
	}
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Role(String id){
    	setId(id);
    }
    
    public String getCode() {
        return this.code;
    }

    public void setCode(String name) {
        this.code = name;
    }

    @JsonIgnore
	public List<Permission> getPermissionsRoles() {
		return permissionsRoles;
	}

	public void setPermissionsRoles(List<Permission> permissionsRoles) {
		this.permissionsRoles = permissionsRoles;
	}

	public List<User> getRoleUsers() {
		return roleUsers;
	}

	public void setRoleUsers(List<User> roleUsers) {
		this.roleUsers = roleUsers;
	}

	public static Role Create_Admin(){
		return new Role(ROLE_ADMIN);
	}
	
	public static Role Create_Seller_Admin(){
		return new Role(ROLE_SELLER_ADMIN);
	}
	
	public static Role Create_Seller(){
		return new Role(ROLE_SELLER);
	}
	
	public static Role Create_Buyer(){
		return new Role(ROLE_BUYER);
	}
	
	public static boolean checkUserIs_Admin(User user){
		return checkUserType(user, Role.Create_Admin());
	}
	
	public static boolean checkUserIs_Seller_Admin(User user){
		return checkUserType(user, Role.Create_Seller_Admin());
	}
	
	public static boolean checkUserIs_Seller(User user){
		return checkUserType(user, Role.Create_Seller());
	}
	
	public static boolean checkUserIs_Buyer(User user){
		return checkUserType(user, Role.Create_Buyer());
	}
	
	public static boolean checkUserType(User user, Role... roles){
		
		for(Role checkRole : roles){
			
			if(user.getRoles().contains(checkRole))
				return true;
				
		}

		return false;
				
	}	
	
}
