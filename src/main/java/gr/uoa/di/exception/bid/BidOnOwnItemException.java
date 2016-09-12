package gr.uoa.di.exception.bid;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class BidOnOwnItemException extends RuntimeException {

    private static final String message = "You can't bid on your own item!";

    public BidOnOwnItemException() {
        super(message);
    }
}

