package gr.uoa.di.exception.user;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class UserNotValidatedException extends AuthenticationException {

    private static final String message = "User has not been validated!";

    public UserNotValidatedException() {
        super(message);
    }
}
