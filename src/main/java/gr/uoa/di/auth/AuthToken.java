package gr.uoa.di.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthToken extends UsernamePasswordAuthenticationToken {
    public AuthToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
