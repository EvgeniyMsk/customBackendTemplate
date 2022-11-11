package ou.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ou.entities.User;
import ou.services.UserService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;

    @Autowired
    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.findByUserName(username);
        if (user.getUsername().equals(username) && user.getPassword().equals(password))
        {
            return new UsernamePasswordAuthenticationToken(
                    userService.loadUserByUsername(username),
                    userService.loadUserByUsername(username).getPassword(),
                    userService.loadUserByUsername(username).getAuthorities()
            );
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
