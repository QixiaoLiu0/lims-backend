package ca.sait.aris.lims.common;

import java.io.Serializable;

public class RespResult<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Integer SUCCESS_CODE = 200;
    private static final Integer ERROR_CODE = 500;
    private static final String SUCCESS_MESSAGE = "Success";
	
	private Integer responseCode;
    private String message;
    private T data;
    
    private RespResult(Integer responseCode, String message, T data) {
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
    }
    
 

    /**
     * successful response(with data)
     */
    public static <T> RespResult<T> success(T data) {
        return new RespResult<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    /**
     * successful response(without data)
     */
    public static <T> RespResult<T> success() {
        return new RespResult<>(SUCCESS_CODE, SUCCESS_MESSAGE, null);
    }

    /**
     * error response(with error msg only)
     */
    public static <T> RespResult<T> error(String msg) {
        return new RespResult<>(ERROR_CODE, msg, null);
    }

    /**
     * error response(custom error codes and error msg)
     */
    public static <T> RespResult<T> error(Integer code, String msg) {
        return new RespResult<>(code, msg, null);
    }

    
    
    
    
    
    
    
    // Getters & Setters 
    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
