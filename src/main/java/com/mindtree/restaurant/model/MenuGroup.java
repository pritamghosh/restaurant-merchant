package com.mindtree.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MenuGroup {
    private String category;
    private List<Menu> menuList = new ArrayList<Menu>();
}
