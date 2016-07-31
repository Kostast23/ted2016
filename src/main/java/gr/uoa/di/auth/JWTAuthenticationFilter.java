package gr.uoa.di.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";

    @Value("${secret_key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication token = new AuthToken();
        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith(BEARER)) {
            String jwt = auth.substring(BEARER.length());
            Jws<Claims> claims = Jwts.parser()
                    .requireSubject("TED")
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt);
            Claims claimsBody = claims.getBody();

            if (claimsBody.getExpiration().after(new Date()) && claimsBody.containsKey("user")) {
                boolean isAdmin = claimsBody.containsKey("admin") && claimsBody.get("admin", Boolean.class);
                token = new AuthToken(claimsBody.get("user", String.class), isAdmin);
            }
        }

        SecurityContextHolder.getContext().setAuthentication(token);

        filterChain.doFilter(request, response);
    }
}
