package com.jellyone.lab1.configuration


import com.jellyone.lab1.configuration.providers.CustomAuthenticationProvider
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.lang.System.Logger
import java.util.*

@Slf4j
@Configuration
@EnableConfigurationProperties
class SecurityConfiguration(
    private val customAuthenticationProvider: CustomAuthenticationProvider,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
    ): DefaultSecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { form ->
                form
                    .loginProcessingUrl("/api/login")
                    .successHandler { req, res, auth ->
                        logger.info("Handling success")
                        res.status = HttpStatus.OK.value()
                    }
                    .failureHandler { req, res, auth ->
                        logger.info("Handling secure failure: $auth")
                        res.status = HttpStatus.UNAUTHORIZED.value()
                    }
                    .usernameParameter("username")
                    .passwordParameter("password")
            }
            .logout { logout ->
                logout
                    .logoutUrl("/api/logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessHandler { req, resp, auth ->
                        resp.status = 200
                    }
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/api/signup",
                        "/api/signin",
                        "/api/admin-requests/submit",
                        "/api/login",
                        "/api/loogout"
                    )
                    .permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .requestMatchers("/api/admin-requests/approve/**").hasRole("ADMIN")
                    .anyRequest()
                    .fullyAuthenticated()
            }
            .httpBasic(withDefaults())
        return http.build()
    }

    @Bean
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Autowired
    fun configureAuthenticationManager(authManagerBuilder: AuthenticationManagerBuilder) {
        authManagerBuilder.authenticationProvider(customAuthenticationProvider)
    }
}