package com.mindtree.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.restaurant.model.Order;
import com.mindtree.restaurant.model.User;
import com.mindtree.restaurant.service.RestaurantService;



@RestController
@CrossOrigin
public class RestaurantController {

    @Autowired
    private RestaurantService service;
    
    @GetMapping("/")
    public String test() {
        return "OK:";
    }
    
    @PostMapping("/order")
    public String placeOrder(@RequestBody Order order) throws Exception {
        service.placeOrder(order);
        return "Order placed Successfully!";

    }
    


	 @PostMapping("/login")
	public User login(@RequestBody User user) throws Exception {
    	return service.login(user);
    	

	}
    


	@ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleError(Exception ex){
        return  new ResponseEntity<>( ex, HttpStatus.BAD_REQUEST);
        
    }
}
