package com.mindtree.restaurant.model;

import lombok.Data;

@Data
public class OrderItem {
    private int qty;
    private Menu menuItem;
    
}
