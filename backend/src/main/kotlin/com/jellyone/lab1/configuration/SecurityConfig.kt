package com.jellyone.lab1.configuration

import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.security.JwtTokenFilter
import com.jellyone.lab1.web.security.JwtTokenProvider
import com.jellyone.lab1.web.security.expression.CustomSecurityExpressionHandler
import com.jellyone.lab1.web.security.principal.IAuthenticationFacade
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
class ApplicationConfig(
    @Lazy private val tokenProvider: JwtTokenProvider,
    private val applicationContext: ApplicationContext,
    private val authenticationFacade: IAuthenticationFacade,
    @Lazy private val userService: UserService,
    private val requestIdFilter: RequestIdFilter
) {

    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .csrf { it.disable() }
            .cors { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling { configurer ->
                configurer.authenticationEntryPoint { request, response, exception ->
                    response.status = HttpStatus.UNAUTHORIZED.value()
                    response.writer.write("Unauthorized.")
                }
                configurer.accessDeniedHandler { request, response, exception ->
                    response.status = HttpStatus.FORBIDDEN.value()
                    response.writer.write("Unauthorized.")
                }
            }
            .authorizeHttpRequests { configurer ->
                configurer
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .requestMatchers("/actuator", "/actuator/**", "/actuator/prometheus", "/**").permitAll()
                    .anyRequest().authenticated()
            }
            .anonymous { it.disable() }
            .addFilterBefore(requestIdFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(
                JwtTokenFilter(tokenProvider, authenticationFacade, userService),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return httpSecurity.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    @Bean
    fun expressionHandler(): MethodSecurityExpressionHandler {
        val expressionHandler = CustomSecurityExpressionHandler()
        expressionHandler.setApplicationContext(applicationContext)
        return expressionHandler
    }
}

