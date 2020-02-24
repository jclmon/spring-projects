package com.jcom.auth.api.service;

import com.jcom.auth.api.model.User;

import pl.jcom.common.service.GenericService;
import rx.Single;

public interface SellerService extends GenericService<User>{
	Single<User> registerOtherSeller(User seller);
}
