package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import com.jellyone.lab1.web.dto.HumanBeingDto
import com.jellyone.lab1.mapper.HumanBeingMapper
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.HumanBeingRepository
import com.jellyone.lab1.web.dto.PutHumanBeingDto
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class HumanBeingService(
    private val humanBeingRepository: HumanBeingRepository,
    private val carRepository: CarRepository
) {

    fun getAllHumans(
        page: Int,
        pageSize: Int,
        sortBy: HumanBeingRepository.HumanBeingFields,
        sortAsc: Boolean,
        name: String?
    ) = humanBeingRepository.findAll(page, pageSize, sortBy, sortAsc, name);


    fun getHumanById(id: Long): HumanBeingDto? {
        return humanBeingRepository.findById(id)?.let { HumanBeingMapper.toDto(it) }
    }

    fun createHuman(humanBeing: CreateHumanBeingRequest): HumanBeingDto {
        val car: Car = carRepository.findById(humanBeing.carId)
            ?: throw IllegalArgumentException("Car not found with id ${humanBeing.carId}")

        val humanBeing = HumanBeingMapper.fromCreateHumanBeingRequestToEntity(humanBeing, car)
        val savedHumanBeing = humanBeingRepository.save(humanBeing)
        return HumanBeingMapper.toDto(savedHumanBeing)
    }

    fun updateHuman(id: Long, humanBeingDto: PutHumanBeingDto): HumanBeingDto? {
        val existingHumanBeing = humanBeingRepository.findById(id) ?: return null

        val car: Car = carRepository.findById(humanBeingDto.carId)
            ?: throw IllegalArgumentException("Car not found with id ${humanBeingDto.carId}")

        val humanBeing = HumanBeingMapper.fromPutHumanBeingToEntity(humanBeingDto.copy(id = existingHumanBeing.id),existingHumanBeing.creationDate, car)
        val updatedHumanBeing = humanBeingRepository.update(humanBeing) ?: return null
        return HumanBeingMapper.toDto(updatedHumanBeing)
    }

    fun deleteHuman(id: Long): Boolean {
        return humanBeingRepository.deleteById(id)
    }

    data class CreateHumanBeingRequest(
        val name: String,
        val x: Double,
        val y: Double,
        val creationDate: LocalDate,
        val realHero: Boolean,
        val hasToothpick: Boolean,
        val carId: Long,
        val mood: Mood?,
        val impactSpeed: Long?,
        val weaponType: WeaponType
    )
}
