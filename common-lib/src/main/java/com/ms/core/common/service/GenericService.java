package com.ms.core.common.service;

import com.ms.core.common.model.PaginatedResponse;

import rx.Single;

public interface GenericService<T>{

	Single<T> getById(String id);

	Single<PaginatedResponse> getAll(String[] sort, int page, int size);

	Single<PaginatedResponse> getByQuery(String column, String query, String[] sort, int page, int size);
	
	Single<T> add(T obj);

	Single<T> edit(T obj);

	Single<T> delete(T object);

}
