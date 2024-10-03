package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.mapper.CarMapper
import com.jellyone.lab1.repository.CarRepository
import org.springframework.stereotype.Service

@Service
class CarService(private val carRepository: CarRepository) {

    fun getAllCars(        page: Int,
                           pageSize: Int,
                           modelFilter: String?,
                           brandFilter: String?,) = carRepository.findAll(page, pageSize, modelFilter, brandFilter)

    fun getCarById(id: Long) = carRepository.findById(id)

    fun createCar(car: CreateCarRequest): Car {
        val savedCar =
            carRepository.save(Car(null, color = car.color, model = car.model, brand = car.brand, cool = car.cool))
        return savedCar
    }

    fun updateCar(id: Long, car: Car): Car? {
        carRepository.findById(id) ?: return null
        val updatedCar = carRepository.update(car) ?: return null
        return updatedCar
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
