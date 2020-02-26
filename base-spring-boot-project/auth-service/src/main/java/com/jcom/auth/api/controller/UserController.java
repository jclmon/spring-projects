package com.jcom.auth.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcom.auth.api.dto.PasswordDto;
import com.jcom.auth.api.dto.UserImageDto;
import com.jcom.auth.api.event.UserEvent;
import com.jcom.auth.api.model.User;
import com.jcom.auth.api.service.UserService;

import pl.jcom.common.async.response.AsyncResponseEntity;
import pl.jcom.common.controller.ControllerBase;
import pl.jcom.common.event.EventBus;
import pl.jcom.common.response.CommonResponse;

@RestController
public class UserController extends ControllerBase {

	@Autowired
    private UserService userService;
	
	@Autowired
	private EventBus eventBus;
	
	@GetMapping
    public AsyncResponseEntity<User> getAll() {
		return makeAsyncResponse(userService.getAll());
    }
	
	@GetMapping("/users/{id}")
    public AsyncResponseEntity<User> getById(@PathVariable("id") String id) {
		return makeAsyncResponse(userService.getById(id));
    }
	
	@PostMapping("/users/changePassword")
	public AsyncResponseEntity<CommonResponse> passwordChange(@ModelAttribute PasswordDto passwordDto) {
		return makeAsyncResponse(
				userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword())
				.map(r->new CommonResponse(1, r)));
	}
	
	@PutMapping("/users")
    public AsyncResponseEntity<User> update(@ModelAttribute User user) {
    	return makeAsyncResponse(userService.edit(user, new UserImageDto()).map(i->{
    		eventBus.publishEvent(new UserEvent(i));
			return i;
		}), HttpStatus.ACCEPTED);
    }
    
	@PostMapping("/updateFullProfile")
    public AsyncResponseEntity<User> updateFullProfile(@ModelAttribute User user, @ModelAttribute UserImageDto imageDto) {
    	return makeAsyncResponse(userService.edit(user, imageDto).map(i->{
    		eventBus.publishEvent(new UserEvent(i));
			return i;
		}), HttpStatus.ACCEPTED);
    }

}
