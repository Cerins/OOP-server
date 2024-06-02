package lu.oop.server.api.configs;

import lu.oop.server.api.filters.JwtRequestFilter;
import lu.oop.server.api.handlers.StandardAccessDeniedHandler;
import lu.oop.server.api.handlers.StandardAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private JwtRequestFilter jwtRequestFilter;

    // Handles the situations where the user does not have a valid permission
    private StandardAccessDeniedHandler accessDeniedHandler;

    // Handles the situation where the token is malformed or is not given
    private StandardAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    SecurityConfiguration(
            JwtRequestFilter jwtRequestFilter,
            StandardAccessDeniedHandler accessDeniedHandler,
            StandardAuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                // How can client register if he doesnt know the available tags
                .requestMatchers("/tags").permitAll()
                .anyRequest()
                .authenticated()
                // Registrating the handlers
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build();
    }


    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }
}