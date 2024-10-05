package com.jellyone.lab1.web.security

import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.security.principal.IAuthenticationFacade
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class JwtTokenFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationFacade: IAuthenticationFacade,
    private val userService: UserService
) : GenericFilterBean() {

    private val allowedPaths = arrayOf("docs", "swagger", "h2", "auth")

    @Throws(Exception::class)
    override fun doFilter(
        servletRequest: ServletRequest,
        servletResponse: ServletResponse,
        filterChain: FilterChain
    ) {
        val httpRequest = servletRequest as HttpServletRequest
        var bearerToken = httpRequest.getHeader("Authorization")

        val uri = httpRequest.requestURI
        if(!uri.contains("/api")) {
            logger.info("Request to static, passing it")
            filterChain.doFilter(servletRequest, servletResponse)
            return
        }
        if (allowedPaths.any { httpRequest.requestURI.contains(it) }) {
            logger.info("Request to /login or /register. Passing it on without checking JWT")
            filterChain.doFilter(servletRequest, servletResponse)
            return
        }

        try {
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                bearerToken = bearerToken.substring(7)
                if (jwtTokenProvider.validateToken(bearerToken)) {
                    val authentication: Authentication? = jwtTokenProvider.getAuthentication(bearerToken)
                    authentication?.let {
                        SecurityContextHolder.getContext().authentication = it
                    }
                }
                val user = userService.getByUsername(authenticationFacade.getAuthName())
                if (user.role == Role.ADMIN) {
                    authenticationFacade.setAdminRole()
                }
            } else {
                val httpResponse = servletResponse as HttpServletResponse
                httpResponse.status = 401
                httpResponse.writer.println("No JWT token")
                return
            }
        } catch (e: SignatureException) {
            val httpResponse = servletResponse as HttpServletResponse
            httpResponse.status = 401
            httpResponse.writer.println("Invalid JWT token")
            return
        } catch (e: MalformedJwtException) {
            val httpResponse = servletResponse as HttpServletResponse
            httpResponse.status = 401
            httpResponse.writer.println("Invalid JWT token")
            return
        } catch (e: ExpiredJwtException) {
            val httpResponse = servletResponse as HttpServletResponse
            httpResponse.status = 401
            httpResponse.writer.println("Invalid JWT token")
            return
        }

        filterChain.doFilter(servletRequest, servletResponse)
    }
}
