package com.jcom.auth.api.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jcom.auth.api.dao.UserDao;
import com.jcom.auth.api.model.Permission;
import com.jcom.auth.api.model.User;
import com.ms.core.common.dao.impl.GenericDaoImpl;
import com.ms.core.common.exception.DataAccessException;

@Repository
//@CacheConfig(cacheResolver="secondaryCacheResolver", keyGenerator="customKeyGenerator")
//@Cacheable(unless="#result == null")
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}
	
	@Override
	@Transactional(readOnly = true, rollbackFor = DataAccessException.class)
	public User findByEmail(String email) throws DataAccessException {
		try {
			  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		      CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);	      
		      Root<User> root = criteriaQuery.from(User.class);
		      criteriaQuery.select(root);
		      criteriaQuery.where(cb.like(root.get("email"), email), cb.equal(root.get("status"), 1));
		      TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
		      List<User> result = query.getResultList();
		      return result.isEmpty() ? null : result.get(0);		     
	            
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}
	
	@Override
    @Transactional(readOnly = true, rollbackFor = DataAccessException.class)
	public User findByUsernameOrEmail(String username, String email) throws DataAccessException {	
		try {
			return entityManager.createQuery(
	    	        "SELECT u FROM "
	    	        + "User u "
	    	        + "left join fetch u.permissions "
	    	        + "WHERE u.nameCode = :userName OR u.email = :email", User.class)
	    	        .setParameter("userName", username)
	    	        .setParameter("email", email)
	    	        .getSingleResult();
		} catch (Exception e) {
			throw new DataAccessException(e);
		}		
	}

	@Override	
	public List<Permission> findUserPermissions() {
		return null;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = DataAccessException.class)
	public User findUserByToken(String token) throws DataAccessException {
		try {
		  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	      CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);	      
	      Root<User> root = criteriaQuery.from(User.class);
	      criteriaQuery.select(root);
	      criteriaQuery.where(cb.equal(root.get("emailVerification.token"), token));
	      TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
	      return query.getSingleResult();
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

}
