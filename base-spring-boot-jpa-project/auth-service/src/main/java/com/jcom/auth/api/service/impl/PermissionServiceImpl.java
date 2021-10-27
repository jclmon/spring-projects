package com.jcom.auth.api.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcom.auth.api.dao.PermissionDao;
import com.jcom.auth.api.model.Permission;
import com.jcom.auth.api.service.PermissionService;
import com.ms.core.common.exception.DataAccessException;
import com.ms.core.common.service.impl.GenericServiceImpl;

import rx.Single;;

@Service
public class PermissionServiceImpl extends GenericServiceImpl<Permission> implements PermissionService{

	@Autowired
	protected PermissionDao permissionDao;
	
	@PostConstruct
	void init() {
        init(Permission.class, permissionDao);
    }
	
	@Override
	public Single<Permission> add(Permission permission){
		try{
			permissionDao.add(permission);
			return Single.just(permission);
		}catch (DataAccessException e) {
			return Single.error(e);
        }
	}
}
