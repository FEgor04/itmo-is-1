package com.jellyone.lab1.web.security.expression


import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.service.UserService
import com.jellyone.lab1.web.security.JwtEntity
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

class CustomMethodSecurityExpressionRoot(
    authentication: Authentication
) : SecurityExpressionRoot(authentication), MethodSecurityExpressionOperations {

    private var filterObject: Any? = null;
    private var returnObject: Any? = null
    private var target: Any? = null
    private var request: HttpServletRequest? = null

    private var userService: UserService? = null

    fun canAccessUser(id: Long): Boolean {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val user: JwtEntity = authentication.principal as JwtEntity
        val userId: Long = user.id

        return userId == id || hasAnyRole(authentication, Role.ADMIN)
    }

    private fun hasAnyRole(authentication: Authentication, vararg roles: Role): Boolean {
        for (role in roles) {
            val authority = SimpleGrantedAuthority(role.name)
            if (authentication.authorities.contains(authority)) {
                return true
            }
        }
        return false
    }

    fun hasAdminRole(): Boolean {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return hasAnyRole(authentication, Role.ADMIN)
    }

    override fun setFilterObject(filterObject: Any?) {
        return filterObject as Unit
    }

    override fun getFilterObject(): Any {
        return filterObject as Unit
    }

    override fun setReturnObject(returnObject: Any?) {
        return returnObject as Unit
    }

    override fun getReturnObject(): Any {
        return returnObject as Unit
    }

    override fun getThis(): Any? {
        return target
    }

    fun setUserService(userService: UserService) {
        this.userService = userService
    }
}