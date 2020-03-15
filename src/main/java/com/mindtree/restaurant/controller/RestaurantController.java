package com.mindtree.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ConfirmBooking placeOrder(@RequestBody Order order) 
    		throws AuthenticationFailureException {
    	return service.placeOrder(order);

    }
    
	 @PostMapping("/login")
	public User login(@RequestBody User user) throws Exception {
    	return service.login(user);
    	

	}
    


	/*@ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleError(Exception ex){
	    ex.printStackTrace();
        return  new ResponseEntity<>( ex, HttpStatus.BAD_REQUEST);
        
    }*/
}
