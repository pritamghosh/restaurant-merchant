package com.mindtree.restaurant.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtree.restaurant.model.OrderItem;
import com.mindtree.restaurant.model.Restaurant;
import com.mindtree.restaurant.model.Restaurants;
import com.mindtree.restaurant.model.User;
import com.mindtree.restaurant.service.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	private static final Map<String, User> userMap = new HashMap<String, User>();
	private static Restaurants restaurents = new Restaurants();
	{
		ObjectMapper mapper = new ObjectMapper();
		try {
			restaurents = mapper.readValue(new File(getClass().getClassLoader().getResource("Restaurent_menu.txt").getFile()), Restaurants.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	{
		try (BufferedReader br = new BufferedReader(
				new FileReader(getClass().getClassLoader().getResource("Restaurent_merchant_details.csv").getFile()))) {
			br.readLine();
			String line = br.readLine();
			while (!StringUtils.isEmpty(line)) {
				String[] restaurant = line.split(",");
				User user = new User();
				user.setRestaurantName(restaurant[0]);
				user.setUsername(restaurant[1]);
				user.setPassword(restaurant[2]);
				user.setEmail(restaurant[3]);
				userMap.put(restaurant[1], user);
				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addOrder(OrderItem order) throws Exception {
	}

	@Override
	public User login(User user) throws Exception {
		User userRes = userMap.get(user.getUsername());
		if(userRes!=null && userRes.getPassword().equals(user.getPassword())) {
			for(Restaurant restaurant : restaurents.getRestaurents()) {
				if(restaurant.getName().equalsIgnoreCase(userRes.getRestaurantName()))
				userRes.setMenuGruop(restaurant.getMenuGruop());
			}
			return userRes;
		}
    	throw new Exception("Bad Credential");
	}

}
