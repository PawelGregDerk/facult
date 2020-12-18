package study.spring.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomLoginHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @SneakyThrows(IOException.class)
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                log.info("admin " + authentication.getName() + " is logged in");
                redirectStrategy.sendRedirect(request, response, "/admin");
                break;
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                log.info("user " + authentication.getName() + " is logged in");
                redirectStrategy.sendRedirect(request, response, "/user");
                break;
            } else {
                throw new IllegalStateException("Role unknown");
            }
        }
    }
}
