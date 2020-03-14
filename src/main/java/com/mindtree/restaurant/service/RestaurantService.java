package com.mindtree.restaurant.service;

import com.mindtree.restaurant.model.Order;
import com.mindtree.restaurant.model.User;

public interface RestaurantService {
    void placeOrder(Order order)throws Exception;

	User login(User user) throws Exception;

}
