package com.auth.bean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auth.util.ParamNullPointerException;
import com.auth.util.ResourceNotAvailible;

/**
 * @Cotroller for Handling Exception
 * @author sanjay.negi
 *
 */
@ControllerAdvice
@RestController
public class HandleAllException extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<CutomExceptionType> handleAllExceptions(Exception ex, WebRequest request) {
		return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A217","Something Went Wrong")
				.build(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(ResourceNotAvailible.class)
	public final ResponseEntity<CutomExceptionType> handleResouceNotExceptions(ResourceNotAvailible ex, WebRequest request) {
		return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A218","This URI Not Availiable")
				.build(),HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler(ParamNullPointerException.class)
	public final ResponseEntity<CutomExceptionType> handleNullPointerExceptions(ResourceNotAvailible ex, WebRequest request) {
		return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A220","Param Or Required Values should not Null or Empty")
				.build(),HttpStatus.NOT_FOUND);
	}
	
}
