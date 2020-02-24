package com.jcom.auth.api.service;

import java.util.List;

import com.jcom.auth.api.dto.UserImageDto;
import com.jcom.auth.api.model.User;

import pl.jcom.common.service.GenericService;
import rx.Single;

public interface UserService extends GenericService<User>{
	
	Single<User> findByEmail(String email);
	
	Single<User> findByUsernameOrEmail(String username, String email);
    
	Single<User> edit(User user, UserImageDto imageDto);
	
	Single<Boolean> delete(String id);

	Single<Boolean> delete(List<String> idList);
    
	Single<User> findUserByToken(String token);

	Single<Boolean> changePassword(String oldPassword, String newPassword);
	
}
