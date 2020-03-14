package com.mindtree.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Order {
    private String restaurantUsername;
    private String email;
    private String  faceId;
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();
    private String restaurantName;
    private String date;
    private String txnId ;
    private float total;
    private String paymentMode;
    
}
