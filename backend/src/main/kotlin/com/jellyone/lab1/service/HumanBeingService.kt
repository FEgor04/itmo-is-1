package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.domain.enums.WeaponType
import com.jellyone.lab1.domain.logs.HumanBeingsLogs
import com.jellyone.lab1.domain.logs.LogAction
import com.jellyone.lab1.exception.OwnerPermissionsConflictException
import com.jellyone.lab1.exception.ResourceNotFoundException
import com.jellyone.lab1.web.dto.HumanBeingDto
import com.jellyone.lab1.mapper.HumanBeingMapper
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.HumanBeingRepository
import com.jellyone.lab1.web.dto.PutHumanBeingDto
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class HumanBeingService(
    private val humanBeingRepository: HumanBeingRepository,
    private val carRepository: CarRepository,
    private val humanBeingMapper: HumanBeingMapper,
    private val userService: UserService,
    private val logsService: LogsService
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

        val humanBeing =
            humanBeingRepository.save(humanBeingMapper.fromCreateHumanBeingRequestToEntity(humanBeing, car))
        val user = userService.getByUserId(humanBeing.ownerId)
        logsService.humansLogsSave(
            HumanBeingsLogs(
                0,
                humanBeing.id!!,
                LogAction.CREATED,
                user!!.username,
                LocalDateTime.now()
            )
        )
        return humanBeing;

    }

    fun updateHuman(id: Long, humanBeingDto: PutHumanBeingDto, username: String): HumanBeing? {
        val owner = userService.getByUsername(username)
        val existingHumanBeing = humanBeingRepository.findById(id) ?: return null
        if (!checkOwner(existingHumanBeing.ownerId, owner.id, owner.role)) {
            throw OwnerPermissionsConflictException()
        }

        val car: Car = carRepository.findById(humanBeingDto.carId)
            ?: throw ResourceNotFoundException("Car not found with id ${humanBeingDto.carId}")

        val humanBeing = humanBeingMapper.fromPutHumanBeingToEntity(
            humanBeingDto.copy(id = existingHumanBeing.id),
            existingHumanBeing.creationDate,
            car,
            owner.id
        )
        logsService.humansLogsSave(HumanBeingsLogs(0, id, LogAction.UPDATED, username, LocalDateTime.now()))
        return humanBeingRepository.update(humanBeing)
    }

    fun deleteHuman(id: Long, username: String): Boolean {
        val user = userService.getByUsername(username)
        if (!checkOwner(id, user.id, user.role)) {
            throw OwnerPermissionsConflictException()
        }
        logsService.humansLogsSave(HumanBeingsLogs(0, id, LogAction.DELETED, username, LocalDateTime.now()))
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
