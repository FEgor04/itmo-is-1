package com.jellyone.lab1.service

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.Coordinates
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.domain.Import
import com.jellyone.lab1.domain.enums.ImportStatus
import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import com.jellyone.lab1.exception.ResourceAlreadyExistsException
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.HumanBeingRepository
import com.jellyone.lab1.repository.ImportRepository
import com.jellyone.lab1.service.props.HumanBeingProperties
import com.jellyone.lab1.web.dto.ImportCsvDataDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ImportService(
    private val humanBeingRepository: HumanBeingRepository,
    private val carRepository: CarRepository,
    private val humanBeingProperties: HumanBeingProperties,
    private val userService: UserService,
    private val importRepository: ImportRepository
) {

    fun getAll(page: Int, pageSize: Int) = importRepository.getAll(page, pageSize)

    fun getAllByUser(page: Int, pageSize: Int, username: String) =
        importRepository.getAllByUser(page, pageSize, username)

    @Transactional
    fun import(inputStream: InputStream, import: Import): Import {
        try {
            val csvMapper = CsvMapper()
            val schema = CsvSchema.builder()
                .setColumnSeparator(',')
                .addColumn("name")
                .addColumn("x")
                .addColumn("y")
                .addColumn("realHero")
                .addColumn("hasToothpick")
                .addColumn("mood")
                .addColumn("speed")
                .addColumn("weaponType")
                .addColumn("car.model")
                .addColumn("car.brand")
                .addColumn("car.color")
                .addColumn("car.cool")
                .addColumn("ownerId")
                .build()

            val reader = csvMapper
                .readerFor(ImportCsvDataDto::class.java)
                .with(schema)

            val importData: List<ImportCsvDataDto> = reader.readValue<List<ImportCsvDataDto>?>(inputStream).toList()

            importData.forEach { dto ->
                val car = Car(
                    color = dto.carColor,
                    model = dto.carModel,
                    brand = dto.carBrand,
                    cool = dto.carCool,
                    ownerId = dto.ownerId
                )

                val savedCar = carRepository.save(car)

                val humanBeing = HumanBeing(
                    name = dto.name,
                    coordinates = Coordinates(dto.x, dto.y),
                    creationDate = LocalDate.now(),
                    realHero = dto.realHero,
                    hasToothpick = dto.hasToothpick,
                    mood = dto.mood?.let { Mood.valueOf(it) },
                    impactSpeed = dto.speed,
                    weaponType = WeaponType.valueOf(dto.weaponType),
                    car = savedCar,
                    ownerId = dto.ownerId
                )
                if (checkNameIsUnique(humanBeing.name)) {
                    throw ResourceAlreadyExistsException("Human with name $humanBeing.name already exists")
                }
                humanBeingRepository.save(humanBeing)
            }
            return updateSuccessfulImport(import, importData.size.toLong())
        } catch (e: Exception) {
            updateFailedImport(import)
            throw e
        }
    }

    fun createProgressImport(username: String): Import {
        val user = userService.getByUsername(username)
        return Import(
            status = ImportStatus.IN_PROGRESS,
            message = null,
            createdEntitiesCount = 0,
            createdAt = LocalDateTime.now(),
            finishedAt = null,
            user = user
        )
    }

    fun updateSuccessfulImport(import: Import, loadedEntitiesCount: Long): Import {
        import.status = ImportStatus.FINISHED
        import.finishedAt = LocalDateTime.now()
        import.createdEntitiesCount = loadedEntitiesCount
        importRepository.update(import)
        return import
    }

    fun updateFailedImport(import: Import): Import {
        import.status = ImportStatus.FAILED
        import.finishedAt = LocalDateTime.now()
        importRepository.update(import)
        return import
    }


    private fun checkNameIsUnique(name: String): Boolean {
        return name != humanBeingProperties.name || humanBeingRepository.countByName(name) == 0L
    }
}
