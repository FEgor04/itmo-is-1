package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.dto.HumanBeingDto
import com.jellyone.lab1.mapper.HumanBeingMapper
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.HumanBeingRepository
import org.springframework.stereotype.Service

@Service
class HumanBeingService(
    private val humanBeingRepository: HumanBeingRepository,
    private val carRepository: CarRepository
) {

    fun getAllHumans(): List<HumanBeingDto> {
        return humanBeingRepository.findAll().map { HumanBeingMapper.toDto(it) }
    }

    fun getHumanById(id: Long): HumanBeingDto? {
        return humanBeingRepository.findById(id)?.let { HumanBeingMapper.toDto(it) }
    }

    fun createHuman(humanBeingDto: HumanBeingDto): HumanBeingDto {
        val car: Car = carRepository.findById(humanBeingDto.carId)
            ?: throw IllegalArgumentException("Car not found with id ${humanBeingDto.carId}")

        val humanBeing = HumanBeingMapper.toEntity(humanBeingDto, car)
        val savedHumanBeing = humanBeingRepository.save(humanBeing)
        return HumanBeingMapper.toDto(savedHumanBeing)
    }

    fun updateHuman(id: Long, humanBeingDto: HumanBeingDto): HumanBeingDto? {
        val existingHumanBeing = humanBeingRepository.findById(id) ?: return null

        val car: Car = carRepository.findById(humanBeingDto.carId)
            ?: throw IllegalArgumentException("Car not found with id ${humanBeingDto.carId}")

        val humanBeing = HumanBeingMapper.toEntity(humanBeingDto.copy(id = existingHumanBeing.id), car)
        val updatedHumanBeing = humanBeingRepository.update(humanBeing) ?: return null
        return HumanBeingMapper.toDto(updatedHumanBeing)
    }

    fun deleteHuman(id: Long): Boolean {
        return humanBeingRepository.deleteById(id)
    }
}
