/**
 * Created as part of Osmosis 2020.
 */
package com.mindtree.restaurant.model;

/**
 * 
 * ConfirmBooking.java Created On:Feb 22, 2020 Created By: M1026329
 */
public class ConfirmBooking {

    private String message;
    private String transactionId;
    
    public ConfirmBooking(String message, String transactionId) {
        super();
        this.message = message;
        this.transactionId = transactionId;
    }
    
    public ConfirmBooking(String message) {
    	super();
        this.message = message;
	}

	public String getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}



	/**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
