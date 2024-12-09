package com.jellyone.lab1.configuration

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.math.min


@Configuration
class MinioConfig(private val properties: MinioProperties) {

    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(properties.host)
            .credentials(properties.username, properties.password)
            .build()
    }

}