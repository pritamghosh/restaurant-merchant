package com.mindtree.restaurant.service;

import com.mindtree.restaurant.model.Order;
import com.mindtree.restaurant.model.Restaurant;
import com.mindtree.restaurant.model.User;

public interface RestaurantService {

    User getUser(User user) throws Exception;

    Restaurant getRestaurantMenu();

    void addOrder(Order order)throws Exception;

}
