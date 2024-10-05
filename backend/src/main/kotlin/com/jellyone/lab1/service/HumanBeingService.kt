package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.domain.enums.WeaponType
import com.jellyone.lab1.exception.OwnerPermissionsConflictException
import com.jellyone.lab1.exception.ResourceNotFoundException
import com.jellyone.lab1.web.dto.HumanBeingDto
import com.jellyone.lab1.mapper.HumanBeingMapper
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.HumanBeingRepository
import com.jellyone.lab1.web.dto.PutHumanBeingDto
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class HumanBeingService(
    private val humanBeingRepository: HumanBeingRepository,
    private val carRepository: CarRepository,
    private val humanBeingMapper: HumanBeingMapper,
    private val userService: UserService
) {

    fun getAllHumans(
        page: Int,
        pageSize: Int,
        sortBy: HumanBeingRepository.HumanBeingFields,
        sortAsc: Boolean,
        name: String?,
        impactSpeedLT: Double?,
    ) = humanBeingRepository.findAll(page, pageSize, sortBy, sortAsc, name, impactSpeedLT);


    fun getHumanById(id: Long): HumanBeing? {
        return humanBeingRepository.findById(id);
    }

    fun createHuman(humanBeing: CreateHumanBeingRequest): HumanBeing {
        val car: Car = carRepository.findById(humanBeing.carId)
            ?: throw ResourceNotFoundException("Car not found with id ${humanBeing.carId}")

        return humanBeingRepository.save(humanBeingMapper.fromCreateHumanBeingRequestToEntity(humanBeing, car))

    }

    fun updateHuman(id: Long, humanBeingDto: PutHumanBeingDto, username: String): HumanBeing? {
        val owner = userService.getByUsername(username)
        if (!checkOwner(id, owner.id, owner.role)) {
            throw OwnerPermissionsConflictException()
        }

        val existingHumanBeing = humanBeingRepository.findById(id) ?: return null

        val car: Car = carRepository.findById(humanBeingDto.carId)
            ?: throw ResourceNotFoundException("Car not found with id ${humanBeingDto.carId}")

        val humanBeing = humanBeingMapper.fromPutHumanBeingToEntity(
            humanBeingDto.copy(id = existingHumanBeing.id),
            existingHumanBeing.creationDate,
            car,
            owner.id
        )
        return humanBeingRepository.update(humanBeing)
    }

    fun deleteHuman(id: Long, username: String): Boolean {
        val user = userService.getByUsername(username)
        if (!checkOwner(id, user.id, user.role)) {
            throw OwnerPermissionsConflictException()
        }
        return humanBeingRepository.deleteById(id)
    }


    fun checkOwner(userId: Long, ownerId: Long, userRole: Role): Boolean {
        if (userRole == Role.ADMIN) {
            return true
        }
        return ownerId == userId
    }

    data class CreateHumanBeingRequest(
        val name: String,
        val x: Double,
        val y: Double,
        val creationDate: LocalDate,
        val realHero: Boolean,
        val hasToothpick: Boolean,
        val carId: Long,
        val mood: Mood?,
        val impactSpeed: Long?,
        val weaponType: WeaponType,
        val ownerId: Long
    )

}
