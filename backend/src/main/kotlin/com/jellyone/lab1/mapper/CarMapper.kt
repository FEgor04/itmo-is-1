package com.jellyone.lab1.mapper

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.web.dto.CarDTO
import com.jellyone.lab1.web.dto.UpdateCarDTO
import org.springframework.stereotype.Component

@Component
object CarMapper {
    fun toDto(car: Car): CarDTO {
        return CarDTO(
            id = car.id!!,
            color = car.color,
            model = car.model,
            brand = car.brand,
            cool = car.cool,
            ownerId = car.ownerId
        )
    }

    fun toEntity(carDto: CarDTO): Car {
        return Car(
            id = carDto.id,
            color = carDto.color,
            model = carDto.model,
            brand = carDto.brand,
            cool = carDto.cool,
            ownerId = carDto.ownerId
        )
    }

    fun toEntityFromUpdate(carDto: UpdateCarDTO): Car {
        return Car(
            id = 0,
            color = carDto.color,
            model = carDto.model,
            brand = carDto.brand,
            cool = carDto.cool,
            ownerId = 0
        )
    }
}