package hexlet.code.config.security;

import hexlet.code.model.User;
import hexlet.code.service.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserAuthenticationService authenticationService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username,
                                       UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        return authenticationService.findByToken(authentication.getCredentials().toString())
                .map(this::buildUserDetails).orElse(null);
    }

    private UserDetails buildUserDetails(final User user) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("USER"));
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
