package gr.uoa.di.exception.bid;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class AuctionFinishedException extends RuntimeException {

    private static final String message = "This auction has finished!";

    public AuctionFinishedException() {
        super(message);
    }
}

