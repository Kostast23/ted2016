package gr.uoa.di.exception.user;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class UserLoginException extends AuthenticationException {

    private static final String message = "Invalid username or password!";

    public UserLoginException() {
        super(message);
    }
}
