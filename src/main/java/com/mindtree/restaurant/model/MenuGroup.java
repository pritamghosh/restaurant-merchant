package com.mindtree.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MenuGroup {
    private String category;
    private List<Menu> menuList = new ArrayList<Menu>();
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<Menu> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<Menu> menuLis) {
		this.menuList = menuLis;
	}
    
    
    
}
