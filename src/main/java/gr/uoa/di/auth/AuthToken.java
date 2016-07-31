package gr.uoa.di.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class AuthToken extends AbstractAuthenticationToken {
    private final Object principal;

    public AuthToken() {
        super(null);
        principal = null;
        setAuthenticated(false);
    }

    public AuthToken(Object principal, boolean isAdmin) {
        super(isAdmin ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")) : null);
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
