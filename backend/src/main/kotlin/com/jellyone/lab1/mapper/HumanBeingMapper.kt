package com.jellyone.lab1.mapper

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.Coordinates
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.web.dto.HumanBeingDto
import com.jellyone.lab1.service.HumanBeingService
import com.jellyone.lab1.web.dto.CarDTO
import com.jellyone.lab1.web.dto.PutHumanBeingDto
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class HumanBeingMapper(
    private val carMapper: CarMapper
) {
    fun toDto(humanBeing: HumanBeing, car: Car): HumanBeingDto {
        return HumanBeingDto(
            id = humanBeing.id,
            name = humanBeing.name,
            x = humanBeing.coordinates.x,
            y = humanBeing.coordinates.y,
            creationDate = humanBeing.creationDate,
            realHero = humanBeing.realHero,
            hasToothpick = humanBeing.hasToothpick,
            car = carMapper.toDto(car),
            mood = humanBeing.mood,
            impactSpeed = humanBeing.impactSpeed,
            weaponType = humanBeing.weaponType,
            ownerId = humanBeing.ownerId
        )
    }

    fun toEntity(humanBeingDto: HumanBeingDto, car: Car): HumanBeing {
        return HumanBeing(
            id = humanBeingDto.id,
            name = humanBeingDto.name,
            coordinates = Coordinates(humanBeingDto.x, humanBeingDto.y),
            creationDate = humanBeingDto.creationDate,
            realHero = humanBeingDto.realHero,
            hasToothpick = humanBeingDto.hasToothpick,
            car = car,
            mood = humanBeingDto.mood,
            impactSpeed = humanBeingDto.impactSpeed,
            weaponType = humanBeingDto.weaponType,
            ownerId = humanBeingDto.ownerId
        )
    }

    fun fromPutHumanBeingToEntity(
        humanBeingDto: PutHumanBeingDto,
        date: LocalDate,
        car: Car,
        ownerId: Long
    ): HumanBeing {
        return HumanBeing(
            id = humanBeingDto.id,
            name = humanBeingDto.name,
            coordinates = Coordinates(humanBeingDto.x, humanBeingDto.y),
            creationDate = date,
            realHero = humanBeingDto.realHero,
            hasToothpick = humanBeingDto.hasToothpick,
            car = car,
            mood = humanBeingDto.mood,
            impactSpeed = humanBeingDto.impactSpeed,
            weaponType = humanBeingDto.weaponType,
            ownerId = ownerId
        )
    }

    fun fromCreateHumanBeingRequestToEntity(
        humanBeingDto: HumanBeingService.CreateHumanBeingRequest,
        car: Car
    ): HumanBeing {
        return HumanBeing(
            name = humanBeingDto.name,
            coordinates = Coordinates(humanBeingDto.x, humanBeingDto.y),
            creationDate = humanBeingDto.creationDate,
            realHero = humanBeingDto.realHero,
            hasToothpick = humanBeingDto.hasToothpick,
            car = car,
            mood = humanBeingDto.mood,
            impactSpeed = humanBeingDto.impactSpeed,
            weaponType = humanBeingDto.weaponType,
            ownerId = humanBeingDto.ownerId
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
}