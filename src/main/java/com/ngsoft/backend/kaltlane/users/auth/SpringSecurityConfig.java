package com.ngsoft.backend.kaltlane.users.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ngsoft.backend.kaltlane.users.auth.filter.JwtAuthenticationFilter;
import com.ngsoft.backend.kaltlane.users.auth.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {

        @Autowired
        private AuthenticationConfiguration authenticationConfiguration;

        /**
         * Method to get the authentication manager from the authentication
         * configuration
         * 
         * @return
         * @throws Exception
         */
        @Bean
        AuthenticationManager authenticationManager() throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.GET, "/api/users/", "api/users/page/{page}")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/users/{id}")
                                                .hasAnyRole("USER", "ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/users/").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                                .addFilter(new JwtValidationFilter(authenticationManager()))
                                .csrf(config -> config.disable())
                                .sessionManagement(management -> management.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(Arrays.asList("*"));
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                return source;
        }

        /**
         * CORS filter bean registration
         * 
         * @return 
         */
        @Bean
        FilterRegistrationBean<CorsFilter> corsFilter() {
                FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(
                                new CorsFilter(this.corsConfigurationSource()));
                bean.setOrder(Ordered.HIGHEST_PRECEDENCE);// Set high precedence
                return bean;
        }

}
