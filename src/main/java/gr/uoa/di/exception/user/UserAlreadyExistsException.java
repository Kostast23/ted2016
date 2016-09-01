package gr.uoa.di.exception.user;

@SuppressWarnings("serial")
public class UserAlreadyExistsException extends RuntimeException {

    private static final String message = "User already exists!";

    public UserAlreadyExistsException() {
        super(message);
    }
}
