package com.jellyone.lab1.configuration


import com.jellyone.lab1.configuration.providers.CustomAuthenticationProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain


@Configuration
@EnableConfigurationProperties
class SecurityConfiguration(
    private val customAuthenticationProvider: CustomAuthenticationProvider
) {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
    ): DefaultSecurityFilterChain {
        http
            .csrf { it.disable() }

            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/signup", "/api/signin", "/api/admin-requests/submit")
                    .permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .requestMatchers("/api/admin-requests/approve/**").hasRole("ADMIN")
                    .anyRequest()
                    .fullyAuthenticated()
            }
            .httpBasic(withDefaults())
            .authenticationProvider(customAuthenticationProvider)


        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

}