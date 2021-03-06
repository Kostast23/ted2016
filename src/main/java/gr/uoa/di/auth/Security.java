package gr.uoa.di.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private JWTAuthenticationFilter jwtFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* security configuration for all endpoints */
        http.csrf().disable()
                .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class).authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/items").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/api/items/suggestions").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.PUT, "/api/items/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/api/items/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/api/items/participating/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/api/items/bought/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/api/bids/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/api/images/upload").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/api/images/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/messages/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/ratings/**").hasAuthority("ROLE_USER")
                .antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().permitAll().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}