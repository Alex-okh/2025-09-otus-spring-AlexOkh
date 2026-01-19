package ru.otus.hw.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            .authorizeHttpRequests(authorizeRequests -> {
                authorizeRequests.requestMatchers("/", "/index.html", "/login", "/style.css")
                                 .permitAll()
                                 .requestMatchers("/books/create", "/books/*/edit", "/book", "/books/*")
                                 .hasAuthority("EDITOR")
                                 .anyRequest()
                                 .authenticated();
            })
            .formLogin(fm -> fm.loginPage("/login")
                               .defaultSuccessUrl("/")
                               .failureUrl("/login?error=true"))
            .logout(logout -> logout.logoutSuccessUrl("/login?logout=true"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
