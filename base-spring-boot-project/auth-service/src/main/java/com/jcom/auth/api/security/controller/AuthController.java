package com.jcom.auth.api.security.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcom.auth.api.security.JwtTokenUtil;
import com.jcom.auth.api.security.dto.JwtAuthenticationDto;
import com.jcom.auth.api.security.dto.JwtResponse;

import pl.jcom.common.controller.ControllerBase;
import pl.jcom.common.exception.UnAuthorizedAccessException;

@RestController
@RequestMapping("/auth")
public class AuthController extends ControllerBase {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	private static Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@PostMapping
	public ResponseEntity<JwtResponse> createAuthenticationToken(
			@ModelAttribute JwtAuthenticationDto jwtAuthenticationDto, Device device)
					throws Exception {
		
	    if (device.isMobile()) {
	        logger.info("Hello mobile user!");
	    } else if (device.isTablet()) {
	        logger.info("Hello tablet user!");
	    } else {
	        logger.info("Hello desktop user!");
	    }

		// Checking username |email and password
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				jwtAuthenticationDto.getUsername(), jwtAuthenticationDto.getPassword()) );

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationDto.getUsername());
		String token = jwtTokenUtil.generateToken(userDetails, device);
		
		Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		return makeResponse(new JwtResponse(token, dateFormat.format(expiration)));

	}

	@RequestMapping("/current")
	public ResponseEntity<UserDetails> getCurrent() throws Exception{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String authenticatedUserName = authentication.getName();
		if(authenticatedUserName.equals("anonymousUser"))
			throw new UnAuthorizedAccessException(authenticatedUserName);
		else
			return makeResponse((UserDetails)authentication.getPrincipal());
	}
	
}
