package com.jellyone.lab1.mapper

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.Coordinates
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.dto.HumanBeingDto
import com.jellyone.lab1.service.HumanBeingService

object HumanBeingMapper {
    fun toDto(humanBeing: HumanBeing): HumanBeingDto {
        return HumanBeingDto(
            id = humanBeing.id,
            name = humanBeing.name,
            x = humanBeing.coordinates.x,
            y = humanBeing.coordinates.y,
            creationDate = humanBeing.creationDate,
            realHero = humanBeing.realHero,
            hasToothpick = humanBeing.hasToothpick,
            carId = humanBeing.car.id!!,
            mood = humanBeing.mood,
            impactSpeed = humanBeing.impactSpeed,
            weaponType = humanBeing.weaponType
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
            weaponType = humanBeingDto.weaponType
        )
    }

    fun fromCreateHumanBeingRequestToEntity(humanBeingDto: HumanBeingService.CreateHumanBeingRequest, car: Car): HumanBeing {
        return HumanBeing(
            name = humanBeingDto.name,
            coordinates = Coordinates(humanBeingDto.x, humanBeingDto.y),
            creationDate = humanBeingDto.creationDate,
            realHero = humanBeingDto.realHero,
            hasToothpick = humanBeingDto.hasToothpick,
            car = car,
            mood = humanBeingDto.mood,
            impactSpeed = humanBeingDto.impactSpeed,
            weaponType = humanBeingDto.weaponType
        )
    }
}