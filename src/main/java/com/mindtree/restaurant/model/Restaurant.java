package com.mindtree.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Restaurant {
    private String name;
    private List<MenuGroup> menuGruop = new ArrayList<MenuGroup>();
}
