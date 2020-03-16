/**
 * 
 */
package com.mindtree.restaurant.client;

import org.springframework.stereotype.Component;

import com.mindtree.restaurant.exception.AuthenticationFailureException;
import com.mindtree.restaurant.exception.InvalidRequestException;
import com.mindtree.restaurant.exception.TransferFailureException;

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
			return new AuthenticationFailureException("Payment authorization failed!!!");
		}
		if(response.status() == 412) {
			return new TransferFailureException("There is no sufficient wallet balance!!!");
		}
		else {
			return new InvalidRequestException("Something went wrong!!!");
		}
	}
}
