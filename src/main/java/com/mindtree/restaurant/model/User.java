package com.mindtree.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class User {
    private String restaurantName;
    private String username;
    private String password;
    private String email;
    private List<MenuGroup> menuGruop = new ArrayList<MenuGroup>();
}
