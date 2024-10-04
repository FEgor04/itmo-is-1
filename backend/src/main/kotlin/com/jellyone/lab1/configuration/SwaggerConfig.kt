package com.jellyone.lab1.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.security.SecurityScheme


@OpenAPIDefinition(
    info = Info(
        title = "Information systems course",
        description = "Sample API of the First lab",
        version = "1.0.0",
    ),
    servers = [
        Server(url = "http://localhost:8080", description = "Default Server URL")
    ],
    tags = [
        Tag(name = "Human Management", description = "API for humans"),
        Tag(name = "Car Management", description = "API for cars"),
        Tag(name = "User Management", description = "API for users")
    ]
)

@SecurityScheme(
    name = "JWT",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class SwaggerConfig {

}