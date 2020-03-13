package com.mindtree.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Restaurant {
    private String name;
    private List<MenuGroup> menuGruop = new ArrayList<MenuGroup>();
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MenuGroup> getMenuGruop() {
		return menuGruop;
	}
	public void setMenuGruop(List<MenuGroup> menuGruop) {
		this.menuGruop = menuGruop;
	}
    
}
