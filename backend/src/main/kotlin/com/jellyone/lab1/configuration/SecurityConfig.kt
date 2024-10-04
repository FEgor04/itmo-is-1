package com.jellyone.lab1.configuration


import com.jellyone.lab1.configuration.providers.CustomAuthenticationProvider
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
import java.util.*

@Slf4j
@Configuration
@EnableConfigurationProperties
class SecurityConfiguration(
    private val customAuthenticationProvider: CustomAuthenticationProvider,
    private val userService: UserService
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


    class CustomAuthenticationFilter(
        private val userService: UserService
    ) : OncePerRequestFilter() {
        @Throws(ServletException::class, IOException::class)
        override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
        ) {
            val path = request.requestURI
            if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") || path == "/swagger-ui.html" ||
                path.contains("/api/signup") || path.contains("/api/signin") || path.contains("/api/admin-requests/submit")
            ) {
                filterChain.doFilter(request, response)
                return
            }


            try {
                var tokenString = request.getHeader("Authorization");
                tokenString = String(Base64.getDecoder().decode(tokenString.split(" ")[1]));
                val username = tokenString.split(":")[0];
                val password = tokenString.split(":")[1];

                if (userService.checkPassword(username, password)) {
                    setAuthenticationToSecurityContextByUsername(username, password);
                    filterChain.doFilter(request, response)
                } else {
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                }
            } catch (e: Exception) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
            }
        }

        private fun setAuthenticationToSecurityContextByUsername(username: String, password: String) {
            val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority(Role.USER.toString()))

            val userDetails = User.withUsername(username)
                .password(password)
                .authorities(authorities)
                .build()

            val authentication = UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.authorities
            )
            SecurityContextHolder.getContext().authentication = authentication
        }
    }

}