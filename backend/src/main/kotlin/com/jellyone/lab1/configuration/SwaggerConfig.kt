package com.jellyone.lab1.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag

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
    ]
)
class SwaggerConfig {
}