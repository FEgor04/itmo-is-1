package com.jellyone.lab1.service.props

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "security.jwt")
data class JwtProperties(
    var secret: String? = null,
    var access: Long? = null,
    var refresh: Long? = null
) {
    @PostConstruct
    fun validate() {
        requireNotNull(secret) { "JWT secret must not be null" }
        requireNotNull(access) { "JWT access time must not be null" }
        requireNotNull(refresh) { "JWT refresh time must not be null" }
    }
}