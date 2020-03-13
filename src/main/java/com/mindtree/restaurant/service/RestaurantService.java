package com.mindtree.restaurant.service;

import com.mindtree.restaurant.model.OrderItem;
import com.mindtree.restaurant.model.User;

public interface RestaurantService {
    void addOrder(OrderItem order)throws Exception;

	User login(User user) throws Exception;

}
