package gr.uoa.di.exception;

import gr.uoa.di.exception.bid.AuctionFinishedException;
import gr.uoa.di.exception.bid.AuctionNotStartedException;
import gr.uoa.di.exception.bid.BidLessThanCurrentException;
import gr.uoa.di.exception.bid.BidOnOwnItemException;
import gr.uoa.di.exception.category.CategoryNotFoundException;
import gr.uoa.di.exception.item.*;
import gr.uoa.di.exception.message.CannotDeleteMessageException;
import gr.uoa.di.exception.message.MessageFieldsException;
import gr.uoa.di.exception.message.MessageNotFoundException;
import gr.uoa.di.exception.user.*;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({UserNotFoundException.class, MessageNotFoundException.class,
            ItemNotFoundException.class, CategoryNotFoundException.class})
    public ResponseEntity<ErrorResponse> resourceNotFound(Exception ex) {
        return new ResponseEntity<ErrorResponse>
                (createErrorResponse(HttpStatus.NOT_FOUND, ex), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({UserLoginException.class, ItemFieldsException.class, ItemDateException.class,
            ItemBuyingPriceException.class, MessageFieldsException.class})
    public ResponseEntity<ErrorResponse> badRequest(Exception ex) {
        return new ResponseEntity<ErrorResponse>
                (createErrorResponse(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotValidatedException.class, AccessException.class})
    public ResponseEntity<ErrorResponse> forbidden(Exception ex) {
        return new ResponseEntity<ErrorResponse>
                (createErrorResponse(HttpStatus.FORBIDDEN, ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ UserAlreadyExistsException.class, UserAlreadyValidatedException.class,
            AuctionFinishedException.class, AuctionNotStartedException.class,
            BidLessThanCurrentException.class, BidOnOwnItemException.class,
            ItemCannotBeEditedException.class, CannotDeleteMessageException.class,
            ItemRatingException.class})
    public ResponseEntity<ErrorResponse> conflict(Exception ex) {
        return new ResponseEntity<ErrorResponse>
                (createErrorResponse(HttpStatus.CONFLICT, ex), HttpStatus.CONFLICT);
    }

    private ErrorResponse createErrorResponse(HttpStatus status, Exception ex) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(status.value());
        response.setError(status.getReasonPhrase());
        response.setMessage(ex.getMessage());
        return response;
    }
}