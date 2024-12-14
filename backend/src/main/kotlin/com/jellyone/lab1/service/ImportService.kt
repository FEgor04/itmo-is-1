package com.jellyone.lab1.service

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.jellyone.lab1.configuration.MinioProperties
import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.Coordinates
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.domain.Import
import com.jellyone.lab1.domain.enums.ImportStatus
import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.domain.enums.WeaponType
import com.jellyone.lab1.exception.OwnerPermissionsConflictException
import com.jellyone.lab1.exception.ResourceAlreadyExistsException
import com.jellyone.lab1.exception.ResourceNotFoundException
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.HumanBeingRepository
import com.jellyone.lab1.repository.ImportRepository
import com.jellyone.lab1.service.props.HumanBeingProperties
import com.jellyone.lab1.web.dto.ImportCsvDataDto
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime


@Service
class ImportService(
    private val humanBeingRepository: HumanBeingRepository,
    private val carRepository: CarRepository,
    private val humanBeingProperties: HumanBeingProperties,
    private val userService: UserService,
    private val importRepository: ImportRepository,
    private val fileService: FileService
) {
    private val validator = Validator()
    private var isNameNotUnique: Boolean = false
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun getAll(page: Int, pageSize: Int) = importRepository.getAll(page, pageSize)

    @Transactional
    fun getAllByUser(page: Int, pageSize: Int, username: String) =
        importRepository.getAllByUser(page, pageSize, username)

    @Transactional
    fun import(inputStream: InputStream, objectSize: Long, import: Import, username: String): Import {
        val user = userService.getByUsername(username)

        val byteArray = ByteArrayOutputStream()
        IOUtils.copy(inputStream, byteArray)
        val bytes: ByteArray = byteArray.toByteArray()

        log.info("Uploaded uncommited file to S3")

        try {
            val csvMapper = CsvMapper()
            val schema = CsvSchema.emptySchema().withHeader()

            val reader = csvMapper.readerFor(ImportCsvDataDto::class.java).with(schema)
            val importData: List<ImportCsvDataDto> =
                reader.readValues<ImportCsvDataDto>(ByteArrayInputStream(bytes)).readAll()

            val humanBeings = mutableListOf<HumanBeing>()
            val cars = mutableListOf<Car>()

            importData.forEach { dto ->
                val car = Car(
                    color = dto.carColor,
                    model = dto.carModel,
                    brand = dto.carBrand,
                    cool = dto.carCool,
                    ownerId = user.id
                )

                val carValidationResult = validator.validateCar(car)
                if (!carValidationResult.isValid) {
                    throw IllegalArgumentException("Car validation failed: ${carValidationResult.message}")
                }

                val humanBeing = HumanBeing(
                    name = dto.name,
                    coordinates = Coordinates(dto.x, dto.y),
                    creationDate = LocalDate.now(),
                    realHero = dto.realHero,
                    hasToothpick = dto.hasToothpick,
                    mood = dto.mood?.let { Mood.valueOf(it) },
                    impactSpeed = dto.speed,
                    weaponType = WeaponType.valueOf(dto.weaponType),
                    ownerId = user.id
                )
                if (checkNameIsNotUnique(humanBeing.name)) {
                    throw ResourceAlreadyExistsException("Human with name ${humanBeing.name} already exists")
                }
                checkNameNotUnique(humanBeing.name)

                val humanBeingValidationResult = validator.validateHumanBeing(humanBeing)
                if (!humanBeingValidationResult.isValid) {
                    throw IllegalArgumentException("HumanBeing validation failed: ${humanBeingValidationResult.message}")
                }

                cars.add(car)
                humanBeings.add(humanBeing)
            }
            val savedCars = carRepository.saveAll(cars)
            humanBeings.forEachIndexed { index, humanBeing ->
                humanBeing.car = savedCars[index]
            }

            humanBeingRepository.saveAll(humanBeings)
//            fileService.commitFile(import.id)
            log.info("Saved import to database")
            return updateSuccessfulImport(import, importData.size.toLong())
        } catch (e: Exception) {
//            fileService.rollbackFile(import.id)
//            updateFailedImport(import, e.message ?: "Unknown error")
            throw e
        }
    }

    fun rollbackFile(id: Long) {
        try {
            fileService.rollbackFile(id)
        } catch (e: Exception) {
            log.error("Could not rollback file, $e")
        }
    }

    @Transactional
    fun createProgressImport(username: String): Import {
        val user = userService.getByUsername(username)
        val import = Import(
            status = ImportStatus.IN_PROGRESS,
            message = null,
            createdEntitiesCount = 0,
            createdAt = LocalDateTime.now(),
            finishedAt = null,
            user = user
        )
        return importRepository.create(import)
    }

    @Transactional
    fun updateSuccessfulImport(import: Import, loadedEntitiesCount: Long): Import {
        import.status = ImportStatus.FINISHED
        import.finishedAt = LocalDateTime.now()
        import.createdEntitiesCount = loadedEntitiesCount
        importRepository.update(import)
        return import
    }

    @Transactional
    fun updateFailedImport(import: Import, message: String): Import {
        import.status = ImportStatus.FAILED
        import.message = message
        import.finishedAt = LocalDateTime.now()
        importRepository.update(import)
        return import
    }

    fun checkOwner(id: Long, username: String) {
        val import = importRepository.findById(id) ?: throw ResourceNotFoundException("Import not found with id $id")
        val user = userService.getByUsername(username)
        if ((import.user.id != user.id) && (user.role != Role.ADMIN)) {
            throw OwnerPermissionsConflictException()
        }
    }

    fun uploadFile(importId: Long, inputStream: InputStream, objectSize: Long) {
        fileService.uploadFile(importId, inputStream, objectSize)
    }

    private fun checkNameIsNotUnique(name: String): Boolean {
        return (name == humanBeingProperties.name && isNameNotUnique) || (name == humanBeingProperties.name && humanBeingRepository.countByName(
            humanBeingProperties.name
        ) != 0L)
    }

    private fun checkNameNotUnique(name: String) {
        if (name == humanBeingProperties.name) {
            isNameNotUnique = true
        }
    }

    private inner class Validator {

        fun validateCar(car: Car): ValidationResult {
            return ValidationResult(true, "")
        }

        fun validateHumanBeing(humanBeing: HumanBeing): ValidationResult {
            if (humanBeing.impactSpeed!! >= 108L) {
                return ValidationResult(false, "Human impact speed cannot be greater than 108.")
            }
            if (humanBeing.name.isBlank()) {
                return ValidationResult(false, "Human name cannot be blank.")
            }
            return ValidationResult(true, "")
        }
    }

    data class ValidationResult(val isValid: Boolean, val message: String)
}

