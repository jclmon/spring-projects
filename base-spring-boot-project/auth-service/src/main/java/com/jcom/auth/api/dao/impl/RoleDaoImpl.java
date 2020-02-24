package com.jcom.auth.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.jcom.auth.api.dao.RoleDao;
import com.jcom.auth.api.model.Role;

import pl.jcom.common.dao.impl.GenericDaoImpl;

@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role> implements RoleDao {

	public RoleDaoImpl() {
		super(Role.class);
	}
	
}
