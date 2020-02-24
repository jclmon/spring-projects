package com.jcom.auth.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.jcom.auth.api.dao.PermissionDao;

import pl.jcom.common.dao.impl.GenericDaoImpl;
import pl.jcom.common.model.Permission;

@Repository
public class PermissionDaoImpl extends GenericDaoImpl<Permission> implements PermissionDao {

	public PermissionDaoImpl() {
		super(Permission.class);
	}
}
