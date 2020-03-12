package com.mindtree.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Order {
    private String email;
    private String  faceId;
    private List<OrderItem> orders = new ArrayList<OrderItem>();
    
}
