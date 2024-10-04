package com.jellyone.lab1.web.security

import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.service.props.JwtProperties
import com.jellyone.lab1.web.dto.auth.JwtResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.parser
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey
import kotlin.collections.HashSet

@Service
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
    private val userDetailsService: UserDetailsService,
    private val userService: UserService
) {

    private lateinit var key: SecretKey

    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(jwtProperties.secret?.toByteArray() ?: ByteArray(32))
    }

    fun createAccessToken(userId: Long, userName: String, role: Role): String {
        val claims = Jwts.claims()
            .setSubject(userName)
            .add("id", userId)
            .add("roles", resolveRoles(HashSet<Role>(listOf(role))))
            .build()

        val now = Date()

        val dateValidity = Date(now.time + jwtProperties.access!!)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(dateValidity)
            .signWith(key)
            .compact()
    }

    private fun resolveRoles(roles: Set<Role>): List<String> {
        return roles.map { it.name }
    }

    fun createRefreshToken(userId: Long, userName: String): String {
        val claims = Jwts.claims()
            .setSubject(userName)
            .add("id", userId)
            .build()
        val now = Date()
        val dateValidity = Date(now.time + jwtProperties.refresh!!)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(dateValidity)
            .signWith(key)
            .compact()
    }

    fun refreshUserTokens(refreshToken: String): JwtResponse {
        if (!validateToken(refreshToken)) {
            throw RuntimeException("Invalid refresh token")
        }
        val userId = getId(refreshToken).toLong()
        val user = userService.getByUserId(userId)

        if (user != null) {
            return JwtResponse(
                userId,
                user.username,
                createAccessToken(userId, user.username, user.role),
                createRefreshToken(userId, user.username)
            )
        } else {
            throw RuntimeException("User not found");
        }
    }

    fun validateToken(token: String): Boolean {
        val claims: Jws<Claims> = parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
        return !claims.payload.expiration.before(Date())
    }

    private fun getId(token: String): String {
        return parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload["id"].toString()
    }

    private fun getUserName(token: String): String {
        return parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload.subject
    }

    fun getAuthentication(token: String): Authentication {
        val userName = getUserName(token)
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(userName)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }
}