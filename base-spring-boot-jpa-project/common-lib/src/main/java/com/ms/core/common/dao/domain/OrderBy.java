package com.ms.core.common.dao.domain;

public enum OrderBy {
	ID("id"), USERID("userId");

	private String OrderByCode;

	private OrderBy(String orderBy) {
		this.OrderByCode = orderBy;
	}

	public String getOrderByCode() {
		return this.OrderByCode;
	}

}