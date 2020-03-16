/**
 * 
 */
package com.mindtree.restaurant.exception;

import java.util.Date;

import lombok.Data;

/**
 * @author M1026334
 *
 */
@Data
public class ExceptionResponseEntity {
	private Date timeStamp;
	private String message;
	private String Details;
	
	public ExceptionResponseEntity(Date timeStamp, String message, String details) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		Details = details;
	}
	
}
