package com.jcom.auth.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcom.auth.api.model.Role;
import com.jcom.auth.api.model.User;
import com.jcom.auth.api.service.UserService;

import pl.jcom.common.async.response.AsyncResponseEntity;
import pl.jcom.common.controller.ControllerBase;

@RestController
@RequestMapping("/buyers")
public class BuyerController extends ControllerBase {

    @Autowired
    private UserService userService;
    
    @PostMapping
    public AsyncResponseEntity<User> save(@ModelAttribute User user) {
    	user.getRoles().add(Role.Create_Buyer());
    	user.setSellerProfile(null);
        return makeAsyncResponse(userService.add(user), HttpStatus.CREATED);
    }

}
