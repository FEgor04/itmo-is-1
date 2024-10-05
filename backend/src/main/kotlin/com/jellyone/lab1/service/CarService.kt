package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.exception.ResourceNotFoundException
import com.jellyone.lab1.mapper.CarMapper
import com.jellyone.lab1.repository.CarRepository
import org.springframework.stereotype.Service

@Service
class CarService(private val carRepository: CarRepository) {

    fun getAllCars(
        page: Int,
        pageSize: Int,
        sortBy: CarRepository.CarFields,
        sortAsc: Boolean,
        modelFilter: String?,
        brandFilter: String?,
    ) = carRepository.findAll(page, pageSize, sortBy, sortAsc, modelFilter, brandFilter)

    fun getCarById(id: Long?) = id?.let { carRepository.findById(it) }

    fun createCar(car: CreateCarRequest): Car {
        val savedCar =
            carRepository.save(Car(null, color = car.color, model = car.model, brand = car.brand, cool = car.cool))
        return savedCar
    }

    fun updateCar(id: Long, car: Car): Car? {
        return carRepository.update(car) ?: throw ResourceNotFoundException("Car not found with id ${id}")
    }

    fun deleteCar(id: Long): Boolean {
        return carRepository.deleteById(id)
    }

    data class CreateCarRequest(
        val color: String,
        val model: String,
        val brand: String,
        val cool: Boolean
    )
}
