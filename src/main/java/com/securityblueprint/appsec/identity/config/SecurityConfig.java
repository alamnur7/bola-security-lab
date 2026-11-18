package com.securityblueprint.appsec.identity.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {

    private static final String ISSUER_URI = "http://localhost:8081/realms/bola-security-lab";

    private static final String JWK_SET_URI = ISSUER_URI + "/protocol/openid-connect/certs";

    private static final String REQUIRED_AUDIENCE = "bola-api";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
       {

       http
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(session -> session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

               )
               .authorizeHttpRequests(auth -> auth
                       .anyRequest().authenticated()

               )
               .oauth2ResourceServer(oauth2 -> oauth2
                       .jwt(jwt -> {
                           //JWT decoder is configured in the bean below.
                       })
                       .authenticationEntryPoint((request, response, authException) -> {
                           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                           response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                           String jsonBody = """
                        {
                            "status": 401,
                            "error": "Unauthorized",
                            "message": "Token tidak valid atau sudah kedaluwarsa"
                        }
                        """;

                           response.getWriter().write(jsonBody);
                       })
               );
       return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withJwkSetUri(JWK_SET_URI)
                .build();
        OAuth2TokenValidator<Jwt> issuerValidator =
                JwtValidators.createDefaultWithIssuer(ISSUER_URI);
        OAuth2TokenValidator<Jwt> audienceValidator = jwt -> {
            List<String> audiences = jwt.getAudience();

            if (audiences != null && audiences.contains(REQUIRED_AUDIENCE)) {
                return OAuth2TokenValidatorResult.success();
            }

            OAuth2Error error = new OAuth2Error(
                    "invalid_token",
                    "Required audience bola-api missing",
                    null
            );
            return OAuth2TokenValidatorResult.failure(error);
        };

        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        issuerValidator,
                        audienceValidator
                )
        );
        return decoder;
    }
}















