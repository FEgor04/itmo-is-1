package com.jellyone.lab1.exception

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import jakarta.validation.ConstraintViolationException
import org.springframework.boot.json.JsonParseException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.*

@ControllerAdvice
@ResponseBody
class ControllerExceptionHandler {

    private val VALIDATION_EXCEPTION = "Validation exception"
    private val INVALID_INPUT_EXCEPTION = "Invalid input"
    private val NOT_FOUND_EXCEPTION = "Not found"
    private val UNAUTHORIZED_EXCEPTION = "Invalid password, email or JWT token!"
    private val ACCESS_DENIED = "You do not have permission!"

    @ExceptionHandler(ResourceNotFoundException::class)
    fun resourceNotFoundException(ex: ResourceNotFoundException, request: WebRequest): ResponseEntity<ErrorMessage> {
        val message = ex.message?.let { ErrorMessage(HttpStatus.NOT_FOUND.value(), Date(), NOT_FOUND_EXCEPTION, it) }
        return ResponseEntity(message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(ex: Exception, request: WebRequest): ResponseEntity<ErrorMessage> {
        val message =
            ex.message?.let { ErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY.value(), Date(), VALIDATION_EXCEPTION, it) }
        return ResponseEntity(message, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(
        value = [JsonParseException::class, HttpMessageNotReadableException::class, IllegalArgumentException::class,
            InvalidDataAccessApiUsageException::class, MethodArgumentTypeMismatchException::class, ConstraintViolationException::class]
    )
    fun jsonParseException(ex: Exception, request: WebRequest): ResponseEntity<ErrorMessage> {
        val message = ex.message?.let {
            ErrorMessage(
                HttpStatus.BAD_REQUEST.value(), Date(), INVALID_INPUT_EXCEPTION,
                it
            )
        }
        return ResponseEntity(message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(
        value = [BadCredentialsException::class, io.jsonwebtoken.security.SignatureException::class,
            MalformedJwtException::class, ExpiredJwtException::class]
    )
    fun badCredentialsException(ex: Exception, request: WebRequest): ResponseEntity<ErrorMessage> {
        val message = ex.message?.let {
            ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(), Date(), UNAUTHORIZED_EXCEPTION,
                it
            )
        }
        return ResponseEntity(message, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(value = [IllegalStateException::class, OwnerPermissionsConflictException::class])
    fun illegalStateExceptionHandler(ex: Exception, request: WebRequest): ResponseEntity<ErrorMessage> {
        val message =
            ex.message?.let { ErrorMessage(HttpStatus.CONFLICT.value(), Date(), request.getDescription(false), it) }
        return ResponseEntity(message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun accessDeniedExceptionHandler(ex: Exception, request: WebRequest): ResponseEntity<ErrorMessage> {
        val message = ex.message?.let { ErrorMessage(HttpStatus.FORBIDDEN.value(), Date(), ACCESS_DENIED, it) }
        return ResponseEntity(message, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(ResourceAlreadyExistsException::class)
    fun handleResourceAlreadyExistsException(
        ex: ResourceAlreadyExistsException,
        request: WebRequest
    ): ResponseEntity<ErrorMessage> {
        val message = ex.message?.let {
            ErrorMessage(
                HttpStatus.CONFLICT.value(), Date(), "Resource already exists",
                it
            )
        }
        return ResponseEntity(message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(Exception::class)
    fun globalExceptionHandler(ex: Exception, request: WebRequest): ResponseEntity<ErrorMessage> {
        val message =
            ex.message?.let {
                ErrorMessage(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), Date(), request.getDescription(false),
                    it
                )
            }
        return ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
