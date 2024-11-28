package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.domain.logs.CarsLogs
import com.jellyone.lab1.domain.logs.LogAction
import com.jellyone.lab1.exception.OwnerPermissionsConflictException
import com.jellyone.lab1.exception.ResourceNotFoundException
import com.jellyone.lab1.mapper.CarMapper
import com.jellyone.lab1.repository.CarRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CarService(
    private val carRepository: CarRepository,
    private val userService: UserService,
    private val logsService: LogsService
) {

    fun getAllCars(
        page: Int,
        pageSize: Int,
        sortBy: CarRepository.CarFields,
        sortAsc: Boolean,
        modelFilter: String?,
        brandFilter: String?,
        color: String?
    ) = carRepository.findAll(page, pageSize, sortBy, sortAsc, modelFilter, brandFilter, color)

    fun getCarById(id: Long?) = id?.let { carRepository.findById(it) }

    fun createCar(car: CreateCarRequest): Car {

        val car = carRepository.save(
            Car(
                null,
                color = car.color,
                model = car.model,
                brand = car.brand,
                cool = car.cool,
                ownerId = car.ownerId
            )
        )
        val user = userService.getByUserId(car.ownerId)
        logsService.carsLogsSave(CarsLogs(0, car.id!!, LogAction.CREATED, user!!.username, LocalDateTime.now()))
        return car;
    }


    fun updateCar(id: Long, car: Car, username: String): Car? {
        val user = userService.getByUsername(username)
        val carFromDb = carRepository.findById(id) ?: return null;
        if (!checkOwner(user.id, carFromDb.ownerId, user.role)) {
            throw OwnerPermissionsConflictException()
        }

        logsService.carsLogsSave(CarsLogs(0, id, LogAction.UPDATED, username, LocalDateTime.now()))

        return carRepository.update(car.copy(ownerId = user.id, id = carFromDb.id))
            ?: throw ResourceNotFoundException("Car not found with id ${id}")

    }

    fun deleteCar(carId: Long, username: String): Boolean {
        val user = userService.getByUsername(username)
        val car = getCarById(carId) ?: throw ResourceNotFoundException("Car not found with id $carId");
        if (!checkOwner(user.id, car.ownerId, user.role)) {
            throw OwnerPermissionsConflictException()
        }

        logsService.carsLogsSave(CarsLogs(0, carId, LogAction.DELETED, username, LocalDateTime.now()))

        return carRepository.deleteById(carId)
    }


    fun checkOwner(userId: Long, ownerId: Long, userRole: Role): Boolean {
        if (userRole == Role.ADMIN) {
            return true
        }
        return ownerId == userId
    }


    data class CreateCarRequest(
        val color: String,
        val model: String,
        val brand: String,
        val cool: Boolean,
        val ownerId: Long
    )
}
