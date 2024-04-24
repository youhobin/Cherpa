package com.cerpha.userservice.common.security;

import com.cerpha.userservice.cerpha.auth.service.AuthService;
import com.cerpha.userservice.common.redis.RedisService;
import com.cerpha.userservice.common.security.jwt.JwtAuthenticationFilter;
import com.cerpha.userservice.common.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;
    private final RedisService redisService;

    public WebSecurity(Environment env,
                       AuthService authService,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       RedisService redisService) {
        this.env = env;
        this.authService = authService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.redisService = redisService;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(authService).passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(new AntPathRequestMatcher("/auth/users/signup", "POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/users/email", "POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/users/email/verify", "POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/users/login", "POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/users/**", "GET")).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(getAuthenticationFilter(authenticationManager))
                .addFilterBefore(new JwtAuthenticationFilter(env, authService, redisService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> {
                    handler.accessDeniedHandler(accessDeniedHandler());
                });

        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, authService, redisService, new JwtTokenProvider(env));
        authenticationFilter.setFilterProcessesUrl("/auth/users/login");
        return authenticationFilter;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
