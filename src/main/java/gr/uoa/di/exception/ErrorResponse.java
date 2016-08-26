package gr.uoa.di.exception;

import java.util.Map;


public class ErrorResponse {

	private int status;
	private String error;
	private String message;
	private Map<String, String> fieldErrors;

	public ErrorResponse() {}

	public ErrorResponse(int status, String error, String message) {
		this.status = status;
		this.error = error;
		this.message = message;
	}

	public ErrorResponse(int status, String error, String message, Map<String, String> fieldErrors) {
	    this(status, error, message);
	    this.fieldErrors = fieldErrors;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}