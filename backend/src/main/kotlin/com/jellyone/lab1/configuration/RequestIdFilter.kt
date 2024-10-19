package com.jellyone.lab1.configuration

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.UUID

@Component
class RequestIdFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            MDC.put("RID", generateShortRequestId())
            MDC.put("UID", generateUidFromUsername())

            chain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }

    fun generateShortRequestId(): String {
        return UUID.randomUUID().toString().substring(0, 8)
    }

    fun generateUidFromUsername(): String? {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication?.name
        return username?.let { generateUid(it).substring(0, 8) }
    }

    fun generateUid(username: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(username.toByteArray())
        return hashBytes.joinToString("") { String.format("%02x", it) }
    }
}