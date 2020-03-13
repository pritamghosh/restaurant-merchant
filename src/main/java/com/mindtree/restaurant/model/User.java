package com.mindtree.restaurant.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
	private String restaurantName;
    private String username;
    @JsonProperty(access=Access.WRITE_ONLY)
    private String password;
    private String email;
    private List<MenuGroup> menuGruop = new ArrayList<MenuGroup>();
	public void setMenuGruop(List<MenuGroup> menuGruop) {
		this.menuGruop = menuGruop;
	}
	public String getRestaurantName() {
		return restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<MenuGroup> getMenuGruop() {
		return menuGruop;
	}
	
	@Override
	public String toString() {
		return "User [restaurantName=" + restaurantName + ", username=" + username + ", password=" + password
				+ ", email=" + email + ", menuGruop=" + menuGruop + "]";
	}
	
}
