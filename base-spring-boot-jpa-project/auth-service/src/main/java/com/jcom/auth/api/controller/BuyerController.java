package com.jcom.auth.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcom.auth.api.dto.UserDto;
import com.jcom.auth.api.event.UserEvent;
import com.jcom.auth.api.model.Role;
import com.jcom.auth.api.model.User;
import com.jcom.auth.api.service.UserService;
import com.ms.core.common.async.response.AsyncResponseEntity;
import com.ms.core.common.controller.ControllerBase;
import com.ms.core.common.event.EventBus;

@RestController
@RequestMapping("/buyers")
public class BuyerController extends ControllerBase {

    @Autowired
    private UserService userService;
    
	@Autowired
	private EventBus eventBus;
    
    @PostMapping
    public AsyncResponseEntity<User> save(@RequestBody UserDto userDto) {
    	User user = new User();
    	user.setCode("buyer");
    	user.setNameCode(userDto.getLogin());
    	user.getRoles().add(Role.Create_Buyer());
    	user.setSellerProfile(null);
    	return makeAsyncResponse(userService.add(user).map(u ->{
    		eventBus.publishEvent(new UserEvent(u));
			return u;
		}), HttpStatus.ACCEPTED);
    }

}
