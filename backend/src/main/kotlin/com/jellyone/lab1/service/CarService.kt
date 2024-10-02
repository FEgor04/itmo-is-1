package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.dto.CarDto
import com.jellyone.lab1.mapper.CarMapper
import com.jellyone.lab1.repository.CarRepository
import org.springframework.stereotype.Service

@Service
class CarService(private val carRepository: CarRepository) {

    fun getAllCars(): List<CarDto> {
        return carRepository.findAll().map { CarMapper.toDto(it) }
    }

    fun getCarById(id: Long): CarDto? {
        return carRepository.findById(id)?.let { CarMapper.toDto(it) }
    }

    fun createCar(carDto: CarDto): CarDto {
        val car = CarMapper.toEntity(carDto)
        val savedCar = carRepository.save(car)
        return CarMapper.toDto(savedCar)
    }

    fun updateCar(id: Long, carDto: CarDto): CarDto? {
        val existingCar = carRepository.findById(id) ?: return null

        val carToUpdate = CarMapper.toEntity(carDto.copy(id = existingCar.id))
        val updatedCar = carRepository.update(carToUpdate) ?: return null
        return CarMapper.toDto(updatedCar)
    }

    fun deleteCar(id: Long): Boolean {
        return carRepository.deleteById(id)
    }
}
