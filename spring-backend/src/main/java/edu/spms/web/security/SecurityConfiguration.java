package edu.spms.web.security;

import edu.spms.web.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import java.util.Arrays;
import java.util.List;

@Configuration @EnableWebSecurity @EnableMethodSecurity
public class SecurityConfiguration {
    @Bean PasswordEncoder passwordEncoder(){return new LegacyAwarePasswordEncoder();}
    @Bean UserDetailsService userDetailsService(UserRepository users){return username->{var account=users.findByUsernameIgnoreCaseAndActiveTrue(username).orElseThrow(()->new org.springframework.security.core.userdetails.UsernameNotFoundException("Account unavailable"));return User.withUsername(account.username).password(account.passwordHash).roles(account.role).build();};}
    @Bean DaoAuthenticationProvider authenticationProvider(UserDetailsService users,PasswordEncoder encoder){var provider=new DaoAuthenticationProvider();provider.setUserDetailsService(users);provider.setPasswordEncoder(encoder);return provider;}
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{return config.getAuthenticationManager();}
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http,TokenStore tokens,DaoAuthenticationProvider provider)throws Exception{
        http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authenticationProvider(provider)
            .exceptionHandling(e->e.authenticationEntryPoint((req,res,ex)->{res.setStatus(401);res.setContentType("application/json");res.getWriter().write("{\"message\":\"Sign in to continue.\"}");}).accessDeniedHandler((req,res,ex)->{res.setStatus(403);res.setContentType("application/json");res.getWriter().write("{\"message\":\"Your account is not authorized for this action.\"}");}))
            .authorizeHttpRequests(a->a.requestMatchers("/api/auth/login","/api/health","/error").permitAll().requestMatchers("/api/users/**","/api/departments/**").hasRole("ADMIN").requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasAnyRole("ADMIN","HR_MANAGER").requestMatchers("/api/payroll/**").hasAnyRole("ADMIN","HR_MANAGER","EMPLOYEE").anyRequest().authenticated())
            .addFilterBefore(new BearerTokenFilter(tokens),UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean CorsConfigurationSource corsConfigurationSource(@Value("${spms.cors.allowed-origins:http://localhost:5173,http://127.0.0.1:5173}") String allowedOrigins){CorsConfiguration c=new CorsConfiguration();c.setAllowedOrigins(Arrays.stream(allowedOrigins.split(",")).map(String::trim).filter(origin->!origin.isEmpty()).toList());c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));c.setAllowedHeaders(List.of("Authorization","Content-Type"));c.setAllowCredentials(false);UrlBasedCorsConfigurationSource s=new UrlBasedCorsConfigurationSource();s.registerCorsConfiguration("/api/**",c);return s;}
}
