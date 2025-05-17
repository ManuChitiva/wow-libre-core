package com.register.wowlibre.infrastructure.security;

import com.register.wowlibre.domain.port.in.jwt.JwtPort;
import com.register.wowlibre.domain.security.UserDetailsServiceCustom;
import com.register.wowlibre.infrastructure.filter.AuthenticationFilter;
import com.register.wowlibre.infrastructure.filter.JwtAuthenticationFilter;
import com.register.wowlibre.infrastructure.util.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.register.wowlibre.domain.constant.Constants.HEADER_TRANSACTION_ID;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceCustom userDetailsServiceCustom;
    private final JwtPort jwtPort;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
                                 UserDetailsServiceCustom userDetailsServiceCustom,
                                 JwtPort jwtPort) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsServiceCustom = userDetailsServiceCustom;
        this.jwtPort = jwtPort;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000",
                "http://127.0.0.1:3000", "http://localhost:3001", "http://127.0.0.1:3001", "https://www.wowlibre.com"
                , "clustercfg.wowlibre.tkfyma.use2.cache.amazonaws.com"));
        corsConfiguration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.DELETE.name()
        ));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.AUTHORIZATION,
                HEADER_TRANSACTION_ID
        ));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                        endpoints -> endpoints.requestMatchers("/api/auth/login").authenticated()
                ).addFilterBefore(new AuthenticationFilter(authenticationProvider(), jwtPort),
                        UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                        "/actuator/health",

                                        //INTERNAL API
                                        "/api/account/create",
                                        "/api/account/password-recovery/request",
                                        "/api/account/password-recovery/confirm",
                                        "/api/account/search",
                                        "/api/resources/banners-home",
                                        "/api/resources/country",
                                        "/api/resources/widget-home",
                                        "/api/resources/server-promos",
                                        "/api/resources/faqs-subscriptions",
                                        "/api/resources/faqs",
                                        "/api/resources/bank/plans",
                                        "/api/resources/benefit",
                                        "/api/resources/benefits-guild",
                                        "/api/resources/experiences",
                                        "/api/resources/plan-acquisition",
                                        "/api/realm",
                                        "/api/guilds",
                                        "/api/guilds/{id}",
                                        "/api/realm/vdp",
                                        //SWAGGER
                                        "/v2/api-docs",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**")
                                .permitAll()
                                .requestMatchers("/api/realm/key", "/api/realm/**", "/api/resources/create/faq",
                                        "/api/resources/delete/faq").hasAuthority(Rol.ADMIN.getName())
                                .requestMatchers("/api/transaction/purchase", "/api/transaction/subscription-benefits"
                                ).hasAuthority(Rol.ADMIN.getName())
                                .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceCustom);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
