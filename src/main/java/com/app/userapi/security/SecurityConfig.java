package com.app.userapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
@EnableJdbcHttpSession
public class SecurityConfig {
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		http
			.authorizeHttpRequests(customizer -> customizer
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
			    .requestMatchers("/error").permitAll()
			    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
			    .defaultSuccessUrl("/", true)
				.permitAll()
			)
			.logout(LogoutConfigurer::permitAll);

		return http.build();
	}

}
