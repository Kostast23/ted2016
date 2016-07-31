package gr.uoa.di;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class LoginException extends AuthenticationException {
    public LoginException() {
        super("Invalid credentials");
    }
}
