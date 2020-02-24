package com.jcom.auth.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcom.auth.api.model.Role;
import com.jcom.auth.api.model.SellerProfile;
import com.jcom.auth.api.model.User;
import com.jcom.auth.api.service.UserService;

import io.swagger.annotations.ApiOperation;
import pl.jcom.common.async.response.AsyncResponseEntity;
import pl.jcom.common.controller.ControllerBase;

@RestController
@RequestMapping("/sellers")
public class SellerController extends ControllerBase {
	
	@Autowired
    private UserService userService;
    
	@PostMapping
	@ApiOperation(value = "Save seller", response = User.class)
    public AsyncResponseEntity<User> save(@ModelAttribute User user) {
    	user.getRoles().add(Role.Create_Seller_Admin());
    	
    	SellerProfile sellerProfile = new SellerProfile();
    	user.setSellerProfile(sellerProfile);
    	
    	user.setBuyerProfile(null);
        return makeAsyncResponse(userService.add(user), HttpStatus.CREATED);
    }


}
