package com.mindtree.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.restaurant.exception.AuthenticationFailureException;
import com.mindtree.restaurant.exception.InvalidRequestException;
import com.mindtree.restaurant.exception.TransferFailureException;
import com.mindtree.restaurant.model.ConfirmBooking;
import com.mindtree.restaurant.model.Order;
import com.mindtree.restaurant.model.User;
import com.mindtree.restaurant.service.RestaurantService;



@RestController
@CrossOrigin
public class RestaurantController {

    @Autowired
    private RestaurantService service;
    
    
    @PostMapping("/order")
	public ResponseEntity<ConfirmBooking> placeOrder(@RequestBody Order order)
			throws AuthenticationFailureException, TransferFailureException, InvalidRequestException {
		ConfirmBooking confirmBooking = service.placeOrder(order);
		return new ResponseEntity<ConfirmBooking>(confirmBooking, HttpStatus.OK);

	}

	 @PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) throws AuthenticationFailureException {
		 User userRes = service.login(user);
    	return new ResponseEntity<User>(userRes, HttpStatus.OK);
    	

	}
    
}
