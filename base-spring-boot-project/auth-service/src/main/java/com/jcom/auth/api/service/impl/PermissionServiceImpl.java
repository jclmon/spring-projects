package com.jcom.auth.api.service.impl;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcom.auth.api.dao.PermissionDao;
import com.jcom.auth.api.service.PermissionService;

import pl.jcom.common.dao.SequenceDao;
import pl.jcom.common.exception.DataAccessException;
import pl.jcom.common.model.Permission;
import pl.jcom.common.service.impl.GenericServiceImpl;
import rx.Single;;

@Service
public class PermissionServiceImpl extends GenericServiceImpl<Permission> implements PermissionService{

	@Autowired
	protected PermissionDao permissionDao;
	
	@Autowired
	protected SequenceDao sequenceDao;
	
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
