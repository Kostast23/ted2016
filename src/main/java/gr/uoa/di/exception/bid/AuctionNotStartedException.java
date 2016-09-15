package gr.uoa.di.exception.bid;

@SuppressWarnings("serial")
public class AuctionNotStartedException extends RuntimeException {

    private static final String message = "This auction not started yet!";

    public AuctionNotStartedException() {
        super(message);
    }
}

