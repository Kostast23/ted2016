package gr.uoa.di.exception.bid;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class BidLessThanCurrentException extends RuntimeException {

    private static final String message = "Your bid must be greater than the current bid!";

    public BidLessThanCurrentException() {
        super(message);
    }
}

