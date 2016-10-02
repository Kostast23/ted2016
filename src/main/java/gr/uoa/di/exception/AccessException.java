package gr.uoa.di.exception;

@SuppressWarnings("serial")
public class AccessException extends RuntimeException {

    private static final String message = "Access denied. No privilages.";

    public AccessException() {
        super(message);
    }
}
