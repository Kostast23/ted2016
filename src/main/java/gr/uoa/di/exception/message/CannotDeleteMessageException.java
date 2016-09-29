package gr.uoa.di.exception.message;

@SuppressWarnings("serial")
public class CannotDeleteMessageException extends RuntimeException {

    private static final String message = "Message cannot be deleted!";

    public CannotDeleteMessageException() {
        super(message);
    }
}
