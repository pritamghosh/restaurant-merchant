/**
 * 
 */
package com.mindtree.restaurant.model;

import javax.validation.constraints.NotNull;

/**
 * @author M1026329
 *
 */
public class PackageBookingDTO {

	@NotNull(message = "partner is a required field")
	String partner;
	@NotNull(message = "amount is a required field")
	int amount;
	
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
