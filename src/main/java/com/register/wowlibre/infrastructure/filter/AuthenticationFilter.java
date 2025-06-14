package com.register.wowlibre.infrastructure.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.wowlibre.domain.constant.Constants;
import com.register.wowlibre.domain.exception.UnauthorizedException;
import com.register.wowlibre.domain.port.in.jwt.JwtPort;
import com.register.wowlibre.domain.security.JwtDto;
import com.register.wowlibre.domain.shared.CustomUserDetails;
import com.register.wowlibre.domain.security.UserLoginModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.register.wowlibre.domain.constant.Constants.CONSTANT_UNIQUE_ID;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String PATH_AUTHENTICATION_FILTER = "/api/auth/login";
    private final AuthenticationProvider authenticationProvider;
    private final JwtPort jwtPort;

    public AuthenticationFilter(AuthenticationProvider authenticationProvider,
                                JwtPort jwtPort) {
        this.authenticationProvider = authenticationProvider;
        this.jwtPort = jwtPort;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(buildPattern(), "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        String transactionId = request.getHeader(Constants.HEADER_TRANSACTION_ID);
        ThreadContext.put(CONSTANT_UNIQUE_ID, transactionId);
        UserLoginModel user = getParamsUser(request, transactionId);
        return authenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(user.username, user.password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException {

        String transactionId = request.getHeader(Constants.HEADER_TRANSACTION_ID);

        Map<String, Object> body = new HashMap<>();
        ThreadContext.put(CONSTANT_UNIQUE_ID, transactionId);

        try {
            final JwtDto jwt = generateToken(authResult);
            body.put("message", "ok");
            body.put("code", 200);
            body.put("data", jwt);
            body.put(Constants.HEADER_TRANSACTION_ID, transactionId);

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(200);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        } catch (Exception e) {
            body.put("error", "invalid data");
            body.put("message", Constants.Errors.CONSTANT_GENERIC_ERROR_MESSAGE);
            body.put("message_trace", e.getMessage());
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Please verify the information provided.");
        body.put("message", Constants.Errors.CONSTANT_GENERIC_ERROR_MESSAGE);
        body.put("message_trace", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }


    private String buildPattern() {
        return PATH_AUTHENTICATION_FILTER;
    }

    private UserLoginModel getParamsUser(HttpServletRequest request, String transactionId) {
        try {
            return new ObjectMapper().readValue(request.getInputStream(), UserLoginModel.class);
        } catch (IOException e) {
            logger.error("Invalid parameters, please check your information");
            throw new UnauthorizedException(Constants.Errors.CONSTANT_GENERIC_ERROR_MESSAGE, transactionId);
        }
    }

    private JwtDto generateToken(Authentication authResult) {
        CustomUserDetails customUserDetails = ((CustomUserDetails) authResult.getPrincipal());
        String token = jwtPort.generateToken(customUserDetails);
        Date expiration = jwtPort.extractExpiration(token);
        String refreshToken = jwtPort.generateRefreshToken(customUserDetails);
        return new JwtDto(customUserDetails.getUserId(), token, refreshToken, expiration,
                customUserDetails.getAvatarUrl(),
                customUserDetails.getLanguage(), customUserDetails.isPendingValidation());
    }
}
