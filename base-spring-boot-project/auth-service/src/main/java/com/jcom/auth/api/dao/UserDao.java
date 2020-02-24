package com.jcom.auth.api.dao;

import java.util.List;

import com.jcom.auth.api.model.User;

import pl.jcom.common.dao.GenericDao;
import pl.jcom.common.exception.DataAccessException;
import pl.jcom.common.model.Permission;

public interface UserDao extends GenericDao<User>{
	
	User findByEmail(String email) throws DataAccessException;

	List<Permission> findUserPermissions()throws DataAccessException;
	
	User findUserByToken(String token)throws DataAccessException;

	User findByUsernameOrEmail(String username, String email) throws DataAccessException;
	
}
