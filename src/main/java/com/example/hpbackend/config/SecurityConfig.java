package com.example.hpbackend.config;

        import com.example.hpbackend.repositories.UserRepository;
        import jakarta.servlet.http.HttpServletResponse;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Qualifier;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
        import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

        import org.springframework.security.authentication.AuthenticationManager;
        import org.springframework.security.authentication.ProviderManager;
        import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
        import org.springframework.security.authorization.AuthorityAuthorizationManager;
        import org.springframework.security.config.Customizer;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.core.userdetails.User;
        import org.springframework.security.core.userdetails.UserDetailsService;
        import org.springframework.security.core.userdetails.UsernameNotFoundException;
        import org.springframework.security.crypto.factory.PasswordEncoderFactories;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.security.web.SecurityFilterChain;
        import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
        import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
        import org.springframework.security.web.authentication.AuthenticationFailureHandler;
        import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
        import org.springframework.web.cors.CorsConfiguration;
        import org.springframework.web.cors.CorsConfigurationSource;
        import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
        import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;
    private final SessionAuthenticationStrategy customSessionAuthenticationStrategy;

    @Autowired
    public SecurityConfig(UserRepository userRepository, @Qualifier("customSessionAuthenticationStrategy") SessionAuthenticationStrategy customSessionAuthenticationStrategy) {
        this.userRepository = userRepository;
        this.customSessionAuthenticationStrategy = customSessionAuthenticationStrategy;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/csrf-token", "/logout", "/userLoggedIn", "/").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // This line restricts access to /admin/** URLs to only ADMIN role

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureHandler(authenticationFailureHandler())
                )
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "customCookieName")
                ).sessionManagement(sessionManagement -> sessionManagement
                        .sessionAuthenticationStrategy(customSessionAuthenticationStrategy)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            com.example.hpbackend.entity.User user = userRepository.findByUsername(username);
            if (user != null) {
                String[] roles;
                if (username.equals("adminUser")) {
                    roles = new String[]{"ADMIN", "USER"};
                } else {
                    roles = new String[]{"USER"};
                }

                return User.withUsername(user.getUsername())
                        .password("{noop}" + user.getPassword())
                        .roles(roles)
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        var access = AuthorityAuthorizationManager.<RequestAuthorizationContext>hasRole("USER");
        var hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        access.setRoleHierarchy(hierarchy);

        http.authorizeHttpRequests(authorize -> authorize.anyRequest().access(access));

        return http.build();

    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Invalid username or password.\"}");
        };
    }

}
