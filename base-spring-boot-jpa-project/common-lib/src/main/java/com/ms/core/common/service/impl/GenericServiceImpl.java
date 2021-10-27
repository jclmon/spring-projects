package com.ms.core.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ms.core.common.dao.GenericDao;
import com.ms.core.common.exception.DataAccessException;
import com.ms.core.common.exception.ResourceNotFoundException;
import com.ms.core.common.model.PaginatedResponse;
import com.ms.core.common.service.GenericService;

import rx.Single;
import rx.exceptions.Exceptions;

public class GenericServiceImpl<T> implements GenericService<T> {
	
	private Class<? extends T> type;
	protected GenericDao<T> genericDao;
	
	protected void init(Class<? extends T> type, GenericDao<T> dao) {
        this.type = type;
        this.genericDao = dao;
    }
	
	@Override
	public Single<T> getById(Long id){
		try {
            return Single.just(genericDao.getById(id)).map(o->{
            	if(o == null)
            		throw Exceptions.propagate(new ResourceNotFoundException(type.getSimpleName() + " : " + id));
            	return o;
            });
        } catch (DataAccessException de) {
        	return Single.error(de);
        } 
	}
	
	@Override
	public Single<PaginatedResponse> getAll(String[] sort, int page, int size){
		try {
            return Single.just(genericDao.getAll(getPageable(page, size, sort))).map(items -> {
            	return PaginatedResponse.builder()
    	                .numberOfItems(items.getTotalElements()).numberOfPages(items.getTotalPages())
    	                .result((List<Object>) items.getContent())
    	                .build();
            });
        } catch (DataAccessException de) {
        	return Single.error(de);
        } 
	}

	@Override
	public Single<PaginatedResponse> getByQuery(String column, String query, String[] sort, int page, int size) {
		try {			
			return Single.just(genericDao.getByQuery(column, query, getPageable(page, size, sort))).map(items -> { 
		        return PaginatedResponse.builder()
		                .numberOfItems(items.getTotalElements()).numberOfPages(items.getTotalPages())
		                .result((List<Object>) items.getContent())
		                .build();				
			});
            
	    } catch (DataAccessException de) {
	    	return Single.error(de);
	    	
	    }
		
    }
	
	/**
	 * Devuelve el paginador
	 * 
	 * @param page
	 * @param size
	 * @param sort
	 * @return
	 */
	protected PageRequest getPageable(int page, int size, String[] sort2) {		
		List<Order> orders = new ArrayList<Sort.Order>();		
		if(sort2 != null && sort2.length > 0) {
			for(int i = 0; i < sort2.length; i++) {
				String predicate = sort2[i];
				String[] prsp = predicate.split(":");
				if(prsp.length == 2) {
					String sortBy = prsp[0];
					String direction = prsp[1];
					Order order = null;
					if (direction.toLowerCase().equals("asc")) {
						order = new Order(Direction.ASC, sortBy);
						orders.add(order);
					}
					if (direction.toLowerCase().equals("desc")) {
						order = new Order(Direction.DESC, sortBy);
						orders.add(order);
					}
				}
			}
		}
		if(orders.isEmpty()) {
			Order order = new Order(Direction.DESC, "id");
			orders.add(order);
		}
		Sort sort = new Sort(orders);
		return new PageRequest(page, size, sort);
	}
		
	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, 
				rollbackFor = DataAccessException.class)
	public Single<T> add(T obj) {
		try {
	        return Single.just(genericDao.add(obj));
	    } catch (DataAccessException de) {
	    	throw Exceptions.propagate(de);
	    } 
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, 
				rollbackFor = DataAccessException.class)
	public Single<T> edit(T obj) {
		try {
	        return Single.just(genericDao.modify(obj));
	    } catch (DataAccessException de) {
	    	throw Exceptions.propagate(de);
	    } 
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, 
				rollbackFor = DataAccessException.class)
	public Single<T> delete(T obj) {
		try {
	        return Single.just(genericDao.delete(obj));
	    } catch (DataAccessException de) {
	    	throw Exceptions.propagate(de);
	    }
	}

}
