package com.ms.core.common.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonAuthenticationTokenFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(CommonAuthenticationTokenFilter.class);
	
    @Value("${jwt.header}")
    private String tokenHeader;
    
    @Autowired
	private RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        
    	String authToken = request.getHeader(this.tokenHeader);

        if (!StringUtils.isEmpty(authToken) && SecurityContextHolder.getContext().getAuthentication() == null) {

        	try{
        		
        		logger.debug("CommonAuthenticationTokenFilter::INFO: Check token validation ");
        		
        		HttpHeaders headers = new HttpHeaders();
        		headers.add("Authorization", authToken);
        		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        	
        		HttpEntity<String> entity = new HttpEntity<>("", headers);
        		
        		ResponseEntity<String> responseEntity = 
        				restTemplate.exchange(
        						"http://auth-service/auth/current"
        						, HttpMethod.POST
        						, entity
        						, String.class);
            	
        		String jsonUserDetails = responseEntity.getBody();
                UserDetails userDetails = prepareUserDetails(jsonUserDetails);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, tokenHeader, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                logger.debug("CommonAuthenticationTokenFilter::INFO: Token validate ");
                
        	}catch(Exception e){
        		logger.error("CommonAuthenticationTokenFilter::ERROR: ",  e);
        	}
        	
        }

        chain.doFilter(request, response);
    }
    
    private UserDetails prepareUserDetails(String jsonUserDetails) throws JsonProcessingException, IOException{
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	JsonNode root = objectMapper.readTree(jsonUserDetails);
    	
    	Long userId = root.get("dbUser").get("id").asLong();
    	String userCode = root.get("dbUser").get("code").asText();
    	String langCode = root.get("dbUser").get("langCode").asText();
    	String username = root.get("dbUser").get("nameCode").asText();
    	boolean isEnabled =  root.get("enabled").asBoolean();
    	
    	List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    	
    	Iterator<JsonNode> authoritiesIterator = root.get("authorities").elements();
    	while(authoritiesIterator.hasNext()){
    		JsonNode authorityNode = authoritiesIterator.next();
    		authorities.add(new SimpleGrantedAuthority(authorityNode.get("authority").asText()));
    	}
    	
    	return new AuthUser(userId, userCode, username, authorities, isEnabled, langCode);
    }
}
