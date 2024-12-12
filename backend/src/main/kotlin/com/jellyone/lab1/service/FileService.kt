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
    fun uploadUncommitedFile(id: Long, stream: InputStream, size: Long) {
        minioClient.putObject(
            PutObjectArgs.builder().bucket(props.bucketName).`object`("${id}_uncommited.csv").contentType("test/csv")
                .stream(stream, size, -1)
                .build()
        )
    }

    fun commitFile(id: Long) {
        minioClient.copyObject(
            CopyObjectArgs.builder().source(CopySource.builder().bucket(props.bucketName).`object`("${id}_uncommited.csv").build())
                .bucket(props.bucketName)
                .`object`("${id}.csv")
                .build()
        )
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(props.bucketName).`object`("${id}_uncommited.csv").build())
    }

    fun rollbackFile(id: Long) {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(props.bucketName).`object`("${id}.csv").build())
    }

    @PostConstruct()
    fun createBucket() {
        if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(props.bucketName).build())) {
            log.info("Bucket ${props.bucketName} already exists, skipping creation")
            return
        }
        log.warn("Bucket ${props.bucketName} doesn't  exist, creating it")
        // @TODO: create public bucket
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(props.bucketName).build())
        log.info("Bucket ${props.bucketName} created")
    }


}