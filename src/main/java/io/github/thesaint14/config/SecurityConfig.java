package io.github.thesaint14.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration 
public class SecurityConfig {

    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public UserDetailsService userDetailsService (PasswordEncoder encoder){
        return new InMemoryUserDetailsManager(
            User.withUsername("ingest-admin")
                    .password(encoder.encode(System.getenv("DATANEXUS_ADMIN_PASSWORD")))
                    .roles("INGEST_ADMIN")
                    .build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ingest/**", "/jobs/**").hasRole("INGEST_ADMIN")
                .anyRequest().permitAll())

            .httpBasic(basic -> {})
            .csrf(csrf ->csrf.disable());
        return http.build();
    }
}