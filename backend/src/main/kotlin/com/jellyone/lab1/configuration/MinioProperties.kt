package com.jellyone.lab1.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "minio")
data class MinioProperties(
    var host: String? = null,
    var username: String? = null,
    var password: String? = null,
    val bucketName: String = "lab1"
)
