package com.mindtree.restaurant.model;

import lombok.Data;

@Data
public class Menu {
    private int price;
    private String name;
    private String description;
    private boolean isVeg;
}
