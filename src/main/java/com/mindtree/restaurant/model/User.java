package com.mindtree.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String restaurantName;
    private String username;
    private String password;
    private String email;
    private final List<MenuGroup> menuGruop = new ArrayList<MenuGroup>();
}
