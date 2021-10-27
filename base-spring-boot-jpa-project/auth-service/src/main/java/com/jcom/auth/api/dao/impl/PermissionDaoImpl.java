package com.jcom.auth.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.jcom.auth.api.dao.PermissionDao;
import com.jcom.auth.api.model.Permission;
import com.ms.core.common.dao.impl.GenericDaoImpl;

@Repository
public class PermissionDaoImpl extends GenericDaoImpl<Permission> implements PermissionDao {

	public PermissionDaoImpl() {
		super(Permission.class);
	}

}
