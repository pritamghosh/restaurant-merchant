/**
 * 
 */
package com.mindtree.restaurant.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author M1026334
 *
 */
@ControllerAdvice
@RestController
public class RestaurantAppResponseEntityExceptioHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AuthenticationFailureException.class)
	public final ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request) {
		ExceptionResponseEntity exceptionResponseEntity = new ExceptionResponseEntity(new Date(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity(exceptionResponseEntity, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(TransferFailureException.class)
	public final ResponseEntity<Object> handleInsufficientFundException(Exception ex, WebRequest request) {
		ExceptionResponseEntity exceptionResponseEntity = new ExceptionResponseEntity(new Date(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity(exceptionResponseEntity, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleInvalidRequestException(Exception ex, WebRequest request) {
		ExceptionResponseEntity exceptionResponseEntity = new ExceptionResponseEntity(new Date(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity(exceptionResponseEntity, HttpStatus.BAD_REQUEST);
	}

}
