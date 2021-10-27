package com.jcom.auth.api.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcom.auth.api.dao.RoleDao;
import com.jcom.auth.api.model.Role;
import com.jcom.auth.api.service.RoleService;
import com.ms.core.common.exception.DataAccessException;
import com.ms.core.common.service.impl.GenericServiceImpl;

import rx.Single;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role> implements RoleService{

	@Autowired
	protected RoleDao roleDao;
	
	@PostConstruct
	void init() {
        init(Role.class, roleDao);
    }
	
	@Override
	public Single<Role> add(Role role){
		try{
			roleDao.add(role);
			return Single.just(role);
		}catch (DataAccessException e) {
			return Single.error(e);
        }
	}
}
