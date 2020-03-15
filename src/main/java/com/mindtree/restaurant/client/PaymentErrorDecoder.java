/**
 * 
 */
package com.mindtree.restaurant.client;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mindtree.restaurant.exception.AuthenticationFailureException;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * @author M1026334
 *
 */
@Component
public class PaymentErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		if(response.status() == 401) {
			return new AuthenticationFailureException("Uthorization failed!!!!");
		}
		return null;
	}

}
