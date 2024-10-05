package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.WeaponType
import com.jellyone.lab1.exception.ResourceNotFoundException
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
    private val carRepository: CarRepository,
    private val humanBeingMapper: HumanBeingMapper
) {

    fun getAllHumans(
        page: Int,
        pageSize: Int,
        sortBy: HumanBeingRepository.HumanBeingFields,
        sortAsc: Boolean,
        name: String?,
        impactSpeedLT: Double?,
    ) = humanBeingRepository.findAll(page, pageSize, sortBy, sortAsc, name, impactSpeedLT);


    fun getHumanById(id: Long): HumanBeing? {
        return humanBeingRepository.findById(id);
    }

    fun createHuman(humanBeing: CreateHumanBeingRequest): HumanBeing {
        val car: Car = carRepository.findById(humanBeing.carId)
            ?: throw ResourceNotFoundException("Car not found with id ${humanBeing.carId}")

        return humanBeingRepository.save(humanBeingMapper.fromCreateHumanBeingRequestToEntity(humanBeing, car))

    }

    fun updateHuman(id: Long, humanBeingDto: PutHumanBeingDto, ownerId: Long): HumanBeing? {
        val existingHumanBeing = humanBeingRepository.findById(id) ?: return null

        val car: Car = carRepository.findById(humanBeingDto.carId)
            ?: throw ResourceNotFoundException("Car not found with id ${humanBeingDto.carId}")

        val humanBeing = humanBeingMapper.fromPutHumanBeingToEntity(
            humanBeingDto.copy(id = existingHumanBeing.id),
            existingHumanBeing.creationDate,
            car,
            ownerId
        )
        return humanBeingRepository.update(humanBeing) ?: return null
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
        val weaponType: WeaponType,
        val ownerId: Long
    )
}
