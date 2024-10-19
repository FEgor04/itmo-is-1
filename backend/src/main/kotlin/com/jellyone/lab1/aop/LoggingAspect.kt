package com.jellyone.lab1.aop

import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Slf4j
@Aspect
@Component
class LoggingAspect {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     *  Pointcut for methods annotated with @Loggable
     */
    @Pointcut("@annotation(com.jellyone.lab1.aop.annotation.Loggable)")
    fun loggableMethods() {
    }

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *) " +
            "|| within(@org.springframework.stereotype.Service *) " +
            "|| within(@org.springframework.web.bind.annotation.RestController *)")
    fun springBeanPointcut() {
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.jellyone.lab1..*) " +
            "|| within(com.jellyone.lab1.service..*) " +
            "|| within(com.jellyone.lab1.controller..*)")
    fun applicationPackagePointcut() {
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    fun logAfterThrowing(joinPoint: JoinPoint, e: Throwable) {
        log.error(
            "Exception in {}.{}() with cause = {}",
            joinPoint.signature.declaringTypeName,
            joinPoint.signature.name,
            e.cause ?: "NULL"
        )
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
//    @Around("applicationPackagePointcut() && springBeanPointcut()")
    @Around("loggableMethods()")
    @Throws(Throwable::class)
    fun logAround(joinPoint: ProceedingJoinPoint): Any? {
        if (log.isDebugEnabled) {
            log.debug(
                "Enter: {}() with argument[] = {}",
                joinPoint.signature.name,
                joinPoint.args.contentToString()
            )
        }
        return try {
            val result = joinPoint.proceed()
            if (log.isDebugEnabled) {
                log.debug(
                    "Exit: {}() with result = {}",
                    joinPoint.signature.name,
                    result
                )
            }
            result
        } catch (e: IllegalArgumentException) {
            log.error(
                "Illegal argument: {} in {}.{}()",
                joinPoint.args.contentToString(),
                joinPoint.signature.declaringTypeName,
                joinPoint.signature.name
            )
            throw e
        }
    }
}