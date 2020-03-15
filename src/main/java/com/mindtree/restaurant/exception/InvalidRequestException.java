/**
 * Created as part of Osmosis 2020.
 */
package com.mindtree.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * InvalidRequestException.java
 * Created On: Feb 22, 2020 Created By: M1026329
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends Exception {

    /**
     * 
     */
    public InvalidRequestException(String msg) {
        super(msg);
    }
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4535460808696331815L;

}
