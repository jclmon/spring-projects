package com.jcom.auth.api.dao;

import java.util.List;

import com.jcom.auth.api.model.Permission;
import com.jcom.auth.api.model.User;
import com.ms.core.common.dao.GenericDao;
import com.ms.core.common.exception.DataAccessException;

public interface UserDao extends GenericDao<User>{
		
	User findByEmail(String email) throws DataAccessException;

	List<Permission> findUserPermissions() throws DataAccessException;
	
	User findUserByToken(String token) throws DataAccessException;

	User findByUsernameOrEmail(String username, String email) throws DataAccessException;
	
}
