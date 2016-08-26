package gr.uoa.di.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import gr.uoa.di.exception.user.UserNotFoundException;


@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFound(Exception ex) {
    	HttpStatus notFoundStatus = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse();
        response.setStatus(notFoundStatus.value());
        response.setError(notFoundStatus.getReasonPhrase());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(response, notFoundStatus);
	}

}