package com.springboot.scrum_tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.scrum_tracker.filters.JwtFilter;
import com.springboot.scrum_tracker.model.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http
				.csrf(customizer->customizer.disable())
					.authorizeHttpRequests(auth -> 
						auth
							.requestMatchers("/api/public/**").permitAll()
							.requestMatchers("/swagger-ui/**").permitAll()
							.requestMatchers("/v3/api-docs/**").permitAll()
							.requestMatchers("/docs/**").permitAll()
							.requestMatchers("/test/**").permitAll()
//							.requestMatchers("/error/**").permitAll()
							.requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
//							.requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
							.anyRequest().authenticated()
						)
//					.httpBasic(Customizer.withDefaults())
					.sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
					.build();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		UserDetails user1= User
//				.withDefaultPasswordEncoder()
//				.username("aashutosh")
//				.password("pass")
//				.roles("TROLE")
//				.build();
//		UserDetails user2= User
//				.withDefaultPasswordEncoder()
//				.username("raj")
//				.password("pass4")
//				.roles("ROLE2")
//				.build();
//		
//		return new InMemoryUserDetailsManager(user1, user2);
//	}
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
