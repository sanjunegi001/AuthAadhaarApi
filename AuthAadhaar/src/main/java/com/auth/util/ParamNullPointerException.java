package com.auth.util;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Null Pointer Exception")
public class ParamNullPointerException extends Exception{

	
	private static final long serialVersionUID = -3332292346834265371L;
	
	

	
}
