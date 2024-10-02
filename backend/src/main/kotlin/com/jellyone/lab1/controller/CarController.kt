package com.jellyone.lab1.controller

import com.jellyone.lab1.dto.CarDto
import com.jellyone.lab1.service.CarService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cars")
class CarController(private val carService: CarService) {

    @GetMapping
    fun getAllCars(): List<CarDto> {
        return carService.getAllCars()
    }

    @GetMapping("/{id}")
    fun getCarById(@PathVariable id: Long): ResponseEntity<CarDto> {
        val carDto = carService.getCarById(id)
        return if (carDto != null) {
            ResponseEntity.ok(carDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createCar(@RequestBody carDto: CarDto): ResponseEntity<CarDto> {
        val createdCar = carService.createCar(carDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar)
    }

    @PutMapping("/{id}")
    fun updateCar(@PathVariable id: Long, @RequestBody carDto: CarDto): ResponseEntity<CarDto> {
        val updatedCar = carService.updateCar(id, carDto)
        return if (updatedCar != null) {
            ResponseEntity.ok(updatedCar)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteCar(@PathVariable id: Long): ResponseEntity<Void> {
        return if (carService.deleteCar(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}