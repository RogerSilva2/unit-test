package br.com.capgemini.rogersilva.unittest.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.capgemini.rogersilva.unittest.exception.NotFoundException;
import br.com.capgemini.rogersilva.unittest.model.User;
import br.com.capgemini.rogersilva.unittest.service.AuthService;
import br.com.capgemini.rogersilva.unittest.service.UserService;

public class AuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    private AuthService authService;

    private UserService userService;

    public AuthFilter(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = getAccessToken(request);

        if (authService.isAccessTokenValid(accessToken)) {
            authenticate(accessToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (Strings.isBlank(accessToken) || !accessToken.startsWith("Bearer ")) {
            return null;
        }
        return accessToken.substring(7, accessToken.length());
    }

    private void authenticate(String accessToken) {
        try {
            User user = userService.findById(authService.getUserId(accessToken));

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(token);
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}