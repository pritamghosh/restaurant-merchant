/**
 * 
 */
package com.mindtree.restaurant.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mindtree.restaurant.exception.AuthenticationFailureException;
import com.mindtree.restaurant.exception.InvalidRequestException;
import com.mindtree.restaurant.exception.TransferFailureException;
import com.mindtree.restaurant.model.ConfirmBooking;
import com.mindtree.restaurant.model.PaymentDTO;

/**
 * @author M1026334
 *
 */
@FeignClient(name="Payment-service", url="${app.api.mintopay}")
public interface RestaurantPaymentClient {

	@PostMapping("/api/makePayment")
	public ConfirmBooking makePayment(@RequestBody PaymentDTO paymentDTO) throws AuthenticationFailureException, TransferFailureException, InvalidRequestException ;
}
