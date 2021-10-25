package com.ms.core.common.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ms.core.common.exception.DataAccessException;

public interface GenericDao<T> {
    /**
     * This method delete given object from the database.
     *
     * @param id - Object id to load
     * @throws DataAccessException - throws if an error occurs
     */
    T getById(Object id) throws DataAccessException;

    /**
     * This method insert a given object to the database.
     *
     * @param object - instance of Object class
     * @throws DataAccessException - throws if an error occurs
     */
    T add(T object) throws DataAccessException;

    /**
     * This method update given object in the database.
     *
     * @param object - instance of Object class
     * @throws DataAccessException - throws if an error occurs
     */
    T modify(T object) throws DataAccessException;
    
    /**
     * This method delete given object from the database.
     *
     * @param object - instance of Object class
     * @throws DataAccessException - throws if an error occurs
     */
    T delete(T object) throws DataAccessException;

    /**
     * This method queries all the objects by pageNumber and pageSize
     *
     * @throws DataAccessException - throws if an error occurs
     */
    public Page<T> getAll(Pageable pageable) throws DataAccessException;

    /**
     * This method queries by filter
     *
     * @throws DataAccessException - throws if an error occurs
     */
    public Page<T> getByQuery(String column, String query, Pageable pageable) throws DataAccessException;

    /**
     * This method queries count the objects
     *
     * @throws DataAccessException - throws if an error occurs
     */
	Long getCount() throws DataAccessException;

    /**
     * This method queries count the objects
     *
     * @throws DataAccessException - throws if an error occurs
     */
	Long getCount(String column, String query) throws DataAccessException;


}
