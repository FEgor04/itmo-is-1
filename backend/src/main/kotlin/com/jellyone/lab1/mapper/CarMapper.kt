package com.jellyone.lab1.mapper

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.web.dto.CarDTO
import org.springframework.stereotype.Component

@Component
object CarMapper {
    fun toDto(car: Car): CarDTO {
        return CarDTO(
            id = car.id!!,
            color = car.color,
            model = car.model,
            brand = car.brand,
            cool = car.cool
        )
    }

    fun toEntity(carDto: CarDTO): Car {
        return Car(
            id = carDto.id,
            color = carDto.color,
            model = carDto.model,
            brand = carDto.brand,
            cool = carDto.cool
        )
    }
}