package com.jcom.auth.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcom.auth.api.dto.PasswordDto;
import com.jcom.auth.api.dto.UserImageDto;
import com.jcom.auth.api.model.User;
import com.jcom.auth.api.service.UserService;

import io.swagger.annotations.ApiOperation;
import pl.jcom.common.async.response.AsyncResponseEntity;
import pl.jcom.common.controller.ControllerBase;
import pl.jcom.common.response.CommonResponse;
import pl.jcom.common.sse.ListenerType;

@RestController
@RequestMapping("/users")
public class UserController extends ControllerBase {

	@Autowired
    private UserService userService;
	
	public UserController() {
		super(ListenerType.USER);
	}
	
	@RequestMapping
    public AsyncResponseEntity<User> getAll() {
		return makeAsyncResponse(userService.getAll());
    }
	
	@RequestMapping("/{id}")
	@ApiOperation(value = "Get user", response = User.class)
    public AsyncResponseEntity<User> getById(@PathVariable("id") String id) {
		return makeAsyncResponse(userService.getById(id));
    }
	
	@PostMapping("/changePassword")
	@ApiOperation(value = "Change Password", response = CommonResponse.class)
	public AsyncResponseEntity<CommonResponse> passwordChange(@ModelAttribute PasswordDto passwordDto) {
		return makeAsyncResponse(
				userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword())
				.map(r->new CommonResponse(1, r)));
	}
	
	@PutMapping
	@ApiOperation(value = "Update user", response = User.class)
    public AsyncResponseEntity<User> update(@ModelAttribute User user) {
    	return makeAsyncResponse(userService.edit(user, new UserImageDto()).map(i->{
			publishMessage(i.getId(), i);
			return i;
		}), HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/updateFullProfile")
    @ApiOperation(value = "Update full profile", response = User.class)
    public AsyncResponseEntity<User> updateFullProfile(@ModelAttribute User user, @ModelAttribute UserImageDto imageDto) {
    	return makeAsyncResponse(userService.edit(user, imageDto).map(i->{
			publishMessage(i.getId(), i);
			return i;
		}), HttpStatus.ACCEPTED);
    }

}
