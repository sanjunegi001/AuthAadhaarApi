package com.auth.util;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED, reason="Resource Not Found")
public class ResourceNotAvailible extends RuntimeException{

	 /**
	  * 
	  */
	 private static final long serialVersionUID = -2581975292273282583L;
}
