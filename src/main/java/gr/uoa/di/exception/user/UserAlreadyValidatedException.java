package gr.uoa.di.exception.user;

public class UserAlreadyValidatedException extends RuntimeException {

    private static final String message = "User has already been validated!";

    public UserAlreadyValidatedException() {
        super(message);
    }
}
