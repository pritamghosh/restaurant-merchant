package com.mindtree.restaurant.service;

import com.mindtree.restaurant.model.ConfirmBooking;
import com.mindtree.restaurant.model.Order;
import com.mindtree.restaurant.model.User;

public interface RestaurantService {
    ConfirmBooking placeOrder(Order order);

	User login(User user) throws Exception;

}
