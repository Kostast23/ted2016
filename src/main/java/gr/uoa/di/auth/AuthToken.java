package gr.uoa.di.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AuthToken extends AbstractAuthenticationToken {
    private final Object principal;

    public AuthToken() {
        super(null);
        principal = null;
        setAuthenticated(false);
    }

    public AuthToken(Object principal, boolean isAdmin) {
        super(getAuthorities(isAdmin));
        this.principal = principal;
        setAuthenticated(true);
    }

    private static List<GrantedAuthority> getAuthorities(boolean isAdmin) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (isAdmin)
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
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
