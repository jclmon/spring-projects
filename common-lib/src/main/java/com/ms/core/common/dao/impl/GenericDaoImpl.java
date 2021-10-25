package com.ms.core.common.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ms.core.common.dao.GenericDao;
import com.ms.core.common.exception.DataAccessException;

@CacheConfig(cacheResolver="primaryCacheResolver")
public class GenericDaoImpl<T> implements GenericDao<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(GenericDaoImpl.class);

	private Class<T> type;

	public GenericDaoImpl(Class<T> type) {
		this.type = type;
	}

	@Override
	@Caching(
				put={@CachePut(key="#object.id")},
				evict={@CacheEvict(cacheResolver="secondaryCacheResolver", allEntries=true)}
			)
    // MANDATORY: Transaction must be created before.
    @Transactional(propagation = Propagation.MANDATORY)
	public T add(T object) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} add", type);
		try {
			this.entityManager.persist(object);
			return object;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	@Caching(
			put={@CachePut(key="#object.id")},
			evict={@CacheEvict(cacheResolver="secondaryCacheResolver", allEntries=true)}
			)
	@Transactional(propagation = Propagation.MANDATORY)
	public T modify(T object) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} modify", type);
		try {
			this.entityManager.merge(object);
			return object;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}
	
	@Override
	@Caching(
			evict = {
					@CacheEvict(key="#object.id"),
					@CacheEvict(cacheResolver="secondaryCacheResolver", allEntries=true)
				}
			)
	@Transactional(propagation = Propagation.MANDATORY)
	public T delete(T object) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} delete", type);
		try {
			this.entityManager.remove(this.entityManager.contains(object) ? object : this.entityManager.merge(object));
			return object;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}

	}

	@Override
	@Cacheable(key="#id" ,unless="#result == null")
	@Transactional(readOnly = true, rollbackFor = DataAccessException.class)
	public T getById(Object id) throws DataAccessException {
		if (logger.isDebugEnabled())
			logger.debug("type {} getById", type);
		try{
			return this.entityManager.find(type, id);
		}catch(Exception e){
			throw new DataAccessException(e);
		}
	}
	
	
	@Override
	public Page<T> getAll(Pageable pageable) throws DataAccessException {
	    long countResult = getCount();
		List<T> fooList = findAll(pageable);		    		    
	    return new PageImpl<>(fooList, pageable, countResult);
	}
	
	@Cacheable(cacheResolver="secondaryCacheResolver", unless="#result == null")
	@Transactional(readOnly = true, rollbackFor = DataAccessException.class)
	private List<T> findAll(Pageable pageable) throws DataAccessException {
		try {
		     int pageNumber = pageable.getPageNumber();
		     int pageSize = pageable.getPageSize();

			 if (logger.isDebugEnabled())
				logger.debug("type {} getAll {} pageNumber {} pageSize {}", type, pageNumber, pageSize);

			  CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		      CriteriaQuery<T> criteriaQuery = cb.createQuery(type);
		      Root<T> root = criteriaQuery.from(type);
		      criteriaQuery.select(root);
		      TypedQuery<T> query = this.entityManager.createQuery(criteriaQuery);
			  query.setFirstResult((pageNumber) * pageSize);
			  query.setMaxResults(pageSize);
		      return query.getResultList();
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}
	

	@Override
	public Page<T> getByQuery(String column, String query, Pageable pageable) throws DataAccessException {
	    long countResult = getCount(column, query);
		List<T> fooList = findByQuery(column, query, pageable);		    		   
	    return new PageImpl<>(fooList, pageable, countResult);			
	}
	
	@Cacheable(cacheResolver="secondaryCacheResolver", unless="#result == null")
	@Transactional(readOnly = true, rollbackFor = DataAccessException.class)
	private List<T> findByQuery(String column, String query, Pageable pageable) throws DataAccessException {
		try {
		     int pageNumber = pageable.getPageNumber();
		     int pageSize = pageable.getPageSize();

			 if (logger.isDebugEnabled())
				logger.debug("type {} getByFilter {} equals {} pageNumber {} pageSize {}", type, column, query, pageNumber, pageSize);

	         CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	         CriteriaQuery<T> criteriaQuery = cb.createQuery(type);
	         Root<T> root = criteriaQuery.from(type);
	         Predicate p = null;	             
	         if(column!=null && query!=null) {
	        	 p = cb.equal(root.get(column), query);
	         }
	         criteriaQuery.select(root).where(p);
	         TypedQuery<T> q = this.entityManager.createQuery(criteriaQuery);
			 q.setFirstResult((pageNumber) * pageSize);
			 q.setMaxResults(pageSize);
	         return q.getResultList();
	          
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
		
	}
	
	@Override
	@Transactional(readOnly = true, rollbackFor = DataAccessException.class)
	public Long getCount(String column, String query) throws DataAccessException {
		try {
	        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
	        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	        
	        Root<T> root = criteriaQuery.from(type);
	        Path<Object> idPath = root.get("id");
	        Expression<Long> countExpression = cb.count(idPath);
	        
	        Predicate p = null;	             
	        if(column!=null && query!=null) {
	        	p = cb.equal(root.get(column), query);
	        }
	        criteriaQuery.select(countExpression).where(p);

	        TypedQuery<Long> typedQuery = this.entityManager.createQuery(criteriaQuery);	        
	        Long count = typedQuery.getSingleResult();	        
	        return count;
	        
		} catch (Exception e) {
			throw new DataAccessException(e);
		}

    }
	
	@Override
	@Transactional(readOnly = true, rollbackFor = DataAccessException.class)
	public Long getCount() throws DataAccessException {

		try {
	        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
	        CriteriaQuery<Long> query = cb.createQuery(Long.class);
	        
	        Root<T> root = query.from(type);
	        Path<Object> idPath = root.get("id");
	        Expression<Long> countExpression = cb.count(idPath);
	        
	        query.select(countExpression);
	        
	        TypedQuery<Long> typedQuery = this.entityManager.createQuery(query);	        
	        Long count = typedQuery.getSingleResult();	        
	        return count;
	        
		} catch (Exception e) {
			throw new DataAccessException(e);
		}

    }
	
}
