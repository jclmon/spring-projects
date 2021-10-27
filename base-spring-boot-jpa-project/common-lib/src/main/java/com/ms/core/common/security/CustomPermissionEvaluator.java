package com.ms.core.common.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)){
            return false;
        }
       // String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
        
        String targetType = (String) targetDomainObject;        
        return hasPrivilege(authentication, targetType.toUpperCase(), permission.toString().toUpperCase());
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
       if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
          return false;
       }
       return hasPrivilege(authentication, targetType.toUpperCase(), permission.toString().toUpperCase());
	}
	
	/**
	 * User grants
	 * 
	 * @param auth
	 * @param targetType
	 * @param permission
	 * @return
	 */
	private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
	    for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
	        if (grantedAuth.getAuthority().startsWith(targetType)) {
	            if (grantedAuth.getAuthority().contains(permission)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
}
