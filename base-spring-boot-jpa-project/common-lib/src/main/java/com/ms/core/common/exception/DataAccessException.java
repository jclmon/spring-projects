
package com.ms.core.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason="Data Access Exception")  //500
public class DataAccessException extends Exception {

	private static final long serialVersionUID = -1227936014166460133L;
	public static final int PROCESSING_FAILED = 1;
    public static final int VALIDATION_FAILED = 2;

    private int code;

    public DataAccessException() {
        super();
    }
    
    public DataAccessException(Exception e) {
        super(e);
    }

    public DataAccessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
