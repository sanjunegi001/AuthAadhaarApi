package com.auth.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;





@Component
public class MyLoginHandler implements AuthenticationSuccessHandler{

	
	
	/**
	 * Used for authentication. It will allow only the user with admin role to get the dashboard 
	 * and other management details. For normal users only search through the data set is allowed.
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication arg2)
			throws IOException, ServletException {
		 User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 System.out.println("sanjaynegi"+authUser.getAuthorities());
		 Collection<GrantedAuthority> auth = authUser.getAuthorities();
		 String role = null;
		 for (GrantedAuthority grantedAuthority : auth) {
			 role = grantedAuthority.getAuthority();
		}
		if (role.equals("ROLE_ADMIN")) {
			 arg1.sendRedirect("demoHome");
		} else{
			 arg1.sendRedirect("esignuser");
		}
	}

}
