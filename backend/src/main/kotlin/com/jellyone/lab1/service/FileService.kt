package com.jellyone.lab1.service

import com.jellyone.lab1.configuration.MinioConfig
import com.jellyone.lab1.configuration.MinioProperties
import io.minio.*
import jakarta.annotation.PostConstruct
import net.sf.saxon.expr.instruct.Copy
import org.apache.commons.collections4.Get
import org.jooq.impl.QOM.Min
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class FileService(
    private val minioClient: MinioClient, private val props: MinioProperties
) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    fun uploadFile(id: Long, stream: InputStream, size: Long) {
        minioClient.putObject(
            PutObjectArgs.builder().bucket(props.bucketName).`object`("${id}.csv").contentType("test/csv")
                .stream(stream, size, -1)
                .build()
        )
    }

    fun rollbackFile(id: Long) {
        minioClient.removeObject(
            RemoveObjectArgs.builder().bucket(props.bucketName).`object`("${id}.csv").build()
        )
    }

    @PostConstruct()
    fun createBucket() {
        if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(props.bucketName).build())) {
            log.info("Bucket ${props.bucketName} already exists, skipping creation")
            return
        }
        log.warn("Bucket ${props.bucketName} doesn't  exist, creating it")

        minioClient.makeBucket(MakeBucketArgs.builder().bucket(props.bucketName).build())

        try {
            val policy = generateBucketPolicy(props.bucketName)

            minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                    .bucket(props.bucketName)
                    .config(policy)
                    .build()
            )
            log.info("Bucket ${props.bucketName} is now public")
        } catch (e: Exception) {
            log.error("Failed to set bucket policy for ${props.bucketName}", e)
        }

        log.info("Bucket ${props.bucketName} created")
    }


    fun getImportFile(id: Long): ByteArray {
        val obj = minioClient.getObject(GetObjectArgs.builder().`object`("$id.csv").bucket(props.bucketName).build())
        return obj.readAllBytes()
    }

    private fun generateBucketPolicy(bucketName: String): String {
        return """
        {
            "Version": "2012-10-17",
            "Statement": [
                {
                    "Effect": "Allow",
                    "Principal": "*",
                    "Action": [
                        "s3:GetObject",
                        "s3:PutObject"
                    ],
                    "Resource": "arn:aws:s3:::$bucketName/*"
                }
            ]
        }
    """.trimIndent()
    }


}