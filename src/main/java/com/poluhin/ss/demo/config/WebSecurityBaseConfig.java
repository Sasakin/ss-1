package com.poluhin.ss.demo.config;

import com.poluhin.ss.demo.domain.security.Role;
import com.poluhin.ss.demo.domain.security.User;
import com.poluhin.ss.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@ConditionalOnExpression("${application.security.base:true}")
public class WebSecurityBaseConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer :: disable)
                .headers(AbstractHttpConfigurer :: disable)
             .authorizeHttpRequests(auth -> auth
                     .requestMatchers(
                             new AntPathRequestMatcher("/"),
                             new AntPathRequestMatcher("/h2-console/**"),
                             PathRequest.toStaticResources().atCommonLocations()).permitAll()
                     .requestMatchers(new AntPathRequestMatcher("/resource/**"))
                     .authenticated()
             )
             .sessionManagement(sessionManagementConfigurer ->
                     sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .httpBasic();
        return http.build();
    }
    private void initUserAdmin() {
        userRepository.save(new User(null, "admin", "admin", Role.ADMIN));
        userRepository.save(new User(null, "user", "user", Role.USER));
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //initUserAdmin();
        return username -> {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return user;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


}
