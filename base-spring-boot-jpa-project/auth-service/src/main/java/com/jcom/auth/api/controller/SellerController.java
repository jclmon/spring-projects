package com.jcom.auth.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcom.auth.api.model.Role;
import com.jcom.auth.api.model.SellerProfile;
import com.jcom.auth.api.model.User;
import com.jcom.auth.api.service.UserService;
import com.ms.core.common.async.response.AsyncResponseEntity;
import com.ms.core.common.controller.ControllerBase;

@RestController
@RequestMapping("/sellers")
public class SellerController extends ControllerBase {
	
	@Autowired
    private UserService userService;
    
	@PostMapping
    public AsyncResponseEntity<User> save(@RequestBody User user) {
    	user.getRoles().add(Role.Create_Seller_Admin());
    	
    	SellerProfile sellerProfile = new SellerProfile();
    	user.setSellerProfile(sellerProfile);
    	
    	user.setBuyerProfile(null);
        return makeAsyncResponse(userService.add(user), HttpStatus.CREATED);
    }


}
