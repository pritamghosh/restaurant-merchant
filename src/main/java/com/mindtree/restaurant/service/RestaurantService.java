package com.mindtree.restaurant.service;

import com.mindtree.restaurant.exception.AuthenticationFailureException;
import com.mindtree.restaurant.exception.InvalidRequestException;
import com.mindtree.restaurant.exception.TransferFailureException;
import com.mindtree.restaurant.model.ConfirmBooking;
import com.mindtree.restaurant.model.Order;
import com.mindtree.restaurant.model.User;

public interface RestaurantService {
	ConfirmBooking placeOrder(Order order)
			throws AuthenticationFailureException, TransferFailureException, InvalidRequestException;

	User login(User user) throws AuthenticationFailureException;

}
