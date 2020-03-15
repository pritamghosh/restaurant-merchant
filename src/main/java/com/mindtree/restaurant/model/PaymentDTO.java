package com.mindtree.restaurant.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class PaymentDTO extends PackageBookingDTO{

	
    @NotNull
    @Email(message = "Email is a required field")
    private String email;
    
    @NotNull(message = "faceId is a required field")
    private String faceId;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFaceId() {
		return faceId;
	}

	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}
    
}
