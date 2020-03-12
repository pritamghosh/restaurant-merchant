package com.mindtree.restaurant.service;

import com.mindtree.restaurant.model.OrderItem;
import com.mindtree.restaurant.model.Restaurant;
import com.mindtree.restaurant.model.User;

public interface RestaurantService {

    User getUser(User user) throws Exception;

    Restaurant getRestaurantMenu();

    void addOrder(OrderItem order)throws Exception;

}
