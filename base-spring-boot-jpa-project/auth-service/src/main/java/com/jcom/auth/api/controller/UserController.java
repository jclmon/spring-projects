package com.jcom.auth.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.jcom.auth.api.dto.PasswordDto;
import com.jcom.auth.api.dto.UserDto;
import com.jcom.auth.api.dto.UserImageDto;
import com.jcom.auth.api.event.UserEvent;
import com.jcom.auth.api.model.Role;
import com.jcom.auth.api.model.SellerProfile;
import com.jcom.auth.api.model.User;
import com.jcom.auth.api.service.UserService;
import com.ms.core.common.async.response.AsyncResponseEntity;
import com.ms.core.common.controller.ControllerBase;
import com.ms.core.common.event.EventBus;
import com.ms.core.common.model.PaginatedResponse;
import com.ms.core.common.response.CommonResponse;

import io.swagger.annotations.ApiOperation;

@RestController
public class UserController extends ControllerBase {

	@Autowired
    private UserService userService;
	
	@Autowired
	private EventBus eventBus;
	
	@GetMapping
    public AsyncResponseEntity<PaginatedResponse> getAll(@RequestParam("sort") String[] sort, @RequestParam("page") int page, @RequestParam("size") int size) {
		return makeAsyncResponse(userService.getAll(sort, page, size));
    }

	@GetMapping("/users/{id}")
	@ApiOperation(value = "Get user by id", response = User.class)
    public AsyncResponseEntity<User> getById(@PathVariable("id") Long id) {
		return makeAsyncResponse(userService.getById(id));
    }

	@GetMapping("/users/login/{id}")
	@ApiOperation(value = "Get user by id", response = User.class)
    public AsyncResponseEntity<User> getByUsernameOrEmail(@PathVariable("id") String id) {
		return makeAsyncResponse(userService.findByUsernameOrEmail(id, id));
    }

	@GetMapping("/users/client/{id}")
	@ApiOperation(value = "Get user by id", response = User.class)
    public AsyncResponseEntity<UserDto> getByClientId(@PathVariable("id") String id) {
		return makeAsyncResponse(userService.getUserDtoById(id));
    }

	@PostMapping("/users/changePassword")
	@ApiOperation(value = "Change password", response = CommonResponse.class)
	public AsyncResponseEntity<CommonResponse> passwordChange(@RequestBody PasswordDto passwordDto) {
		return makeAsyncResponse(
				userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword())
				.map(r->new CommonResponse(1, r)));
	}
	
	@PutMapping("/users")
	@ApiOperation(value = "Update password", response = User.class)
    public AsyncResponseEntity<User> update(@RequestBody User user) {
    	return makeAsyncResponse(userService.edit(user, new UserImageDto()).map(u ->{
    		eventBus.publishEvent(new UserEvent(u));
			return u;
		}), HttpStatus.ACCEPTED);
    }
    
	@PostMapping("/users/updateFullProfile")
	@ApiOperation(value = "Update profile", response = User.class)
    public AsyncResponseEntity<User> updateFullProfile(@RequestPart User user, @RequestPart UserImageDto imageDto) {
    	return makeAsyncResponse(userService.edit(user, imageDto).map(u ->{
    		eventBus.publishEvent(new UserEvent(u));
			return u;
		}), HttpStatus.ACCEPTED);
    }

	@PostMapping("/users/account")
	@ApiOperation(value = "Create profile", response = User.class)
    public AsyncResponseEntity<User> createProfile(@RequestBody UserDto userDto) {
		User user = new User();
		user.setCode("seller");
		user.setNameCode(userDto.getLogin());
		user.setFirstName(userDto.getFirstname());
		user.setLangCode(userDto.getLanguage());
		user.setPassword(userDto.getPassword());
		user.setEmail(userDto.getEmail());
    	user.getRoles().add(Role.Create_Seller());
    	SellerProfile sellerProfile = new SellerProfile();
    	user.setSellerProfile(sellerProfile);    	
    	user.setBuyerProfile(null);
    	return makeAsyncResponse(userService.add(user).map(u ->{
    		eventBus.publishEvent(new UserEvent(u));
			return u;
		}), HttpStatus.ACCEPTED);
    }
	
}
