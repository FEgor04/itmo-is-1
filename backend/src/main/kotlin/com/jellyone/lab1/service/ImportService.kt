package com.jellyone.lab1.service

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.Coordinates
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import com.jellyone.lab1.exception.ResourceAlreadyExistsException
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.HumanBeingRepository
import com.jellyone.lab1.service.props.HumanBeingProperties
import com.jellyone.lab1.web.dto.ImportCsvDataDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.time.LocalDate

@Service
class ImportService(
    private val humanBeingRepository: HumanBeingRepository,
    private val carRepository: CarRepository,
    private val humanBeingProperties: HumanBeingProperties
) {

    @Transactional
    fun import(inputStream: InputStream) {
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
    }

    private fun checkNameIsUnique(name: String): Boolean {
        return name != humanBeingProperties.name || humanBeingRepository.countByName(name) == 0L
    }
}
