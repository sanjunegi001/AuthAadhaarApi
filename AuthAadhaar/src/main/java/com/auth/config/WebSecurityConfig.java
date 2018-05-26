package com.auth.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.auth.service.UserService;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	

	@Autowired
	private UserService userService;
	
	 @Value("${security.enable-csrf}")
	    private boolean csrfEnabled;
	
	@Autowired
	private MyLoginHandler myLoginHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("sanjayhhh");
		if(!csrfEnabled)
	       {
			http.csrf().disable();
	       }
		 http.authorizeRequests()
		.antMatchers("/loginprocess").authenticated()
		.antMatchers("/demoHome").authenticated()
		
					.and()
					 	.formLogin().loginPage("/login")
							.usernameParameter("username").passwordParameter("password")
								.successHandler(myLoginHandler)  
								.and()
								.logout().logoutSuccessUrl("/login?logout")  
								.permitAll()
								 .and();
								   
//									.csrf().and().headers().defaultsDisabled().cacheControl();
	
	
		
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//auth.inMemoryAuthentication().withUser("admin").password("1234").roles("ROLE_ADMIN");
		auth.userDetailsService(userService);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/fonts/**", "/image/**", "/js/**");
		
	}
	
	
}