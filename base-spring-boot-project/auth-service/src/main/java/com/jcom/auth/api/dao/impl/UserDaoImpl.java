package com.jcom.auth.api.dao.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.jcom.auth.api.dao.UserDao;
import com.jcom.auth.api.model.User;

import pl.jcom.common.dao.impl.GenericDaoImpl;
import pl.jcom.common.exception.DataAccessException;
import pl.jcom.common.model.Permission;

@Repository
@CacheConfig(cacheResolver="secondaryCacheResolver", keyGenerator="customKeyGenerator")
@Cacheable(unless="#result == null")
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao{

	public UserDaoImpl() {
		super(User.class);
	}
	
	@Override
	public User findByEmail(String email) throws DataAccessException {
		Query query = new Query(Criteria.where("email").is(email).and("status").is(1));
		return mongoOperations.findOne(query, User.class);
	}
	
	@Override
	public User findByUsernameOrEmail(String username, String email) throws DataAccessException {	
		Query query = new Query(
				Criteria
				.where("status").ne(-1)
				.orOperator(
						Criteria.where("username").is(username)
						,Criteria.where("email").is(email)
						)
				);
		
		return mongoOperations.findOne(query, User.class);
	}

	@Override
	public List<Permission> findUserPermissions() {
		return null;
	}

	@Override
	public User findUserByToken(String token) throws DataAccessException {
		Query query = new Query(Criteria.where("emailVerification.token").is(token));
		return mongoOperations.findOne(query, User.class);
	}
}
