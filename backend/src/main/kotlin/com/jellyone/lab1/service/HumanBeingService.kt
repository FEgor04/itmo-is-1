package com.jellyone.lab1.service

import com.jellyone.lab1.domain.Car
import com.jellyone.lab1.domain.HumanBeing
import com.jellyone.lab1.domain.enums.Mood
import com.jellyone.lab1.domain.enums.Role
import com.jellyone.lab1.domain.enums.WeaponType
import com.jellyone.lab1.domain.logs.HumanBeingsLogs
import com.jellyone.lab1.domain.logs.LogAction
import com.jellyone.lab1.exception.OwnerPermissionsConflictException
import com.jellyone.lab1.exception.ResourceAlreadyExistsException
import com.jellyone.lab1.exception.ResourceNotFoundException
import com.jellyone.lab1.mapper.HumanBeingMapper
import com.jellyone.lab1.repository.CarRepository
import com.jellyone.lab1.repository.HumanBeingRepository
import com.jellyone.lab1.service.props.HumanBeingProperties
import com.jellyone.lab1.web.dto.PutHumanBeingDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class HumanBeingService(
    private val humanBeingRepository: HumanBeingRepository,
    private val carRepository: CarRepository,
    private val humanBeingMapper: HumanBeingMapper,
    private val userService: UserService,
    private val logsService: LogsService,
    private val humanBeingProperties: HumanBeingProperties
) {
    @Transactional
    fun getAllHumans(
        page: Int,
        pageSize: Int,
        sortBy: HumanBeingRepository.HumanBeingFields,
        sortAsc: Boolean,
        name: String?,
        impactSpeedLT: Double?,
    ) = humanBeingRepository.findAll(page, pageSize, sortBy, sortAsc, name, impactSpeedLT);

    @Transactional
    fun getHumanById(id: Long): HumanBeing? {
        return humanBeingRepository.findById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun createHuman(humanBeing: CreateHumanBeingRequest): HumanBeing {
        val car: Car = carRepository.findById(humanBeing.carId)
            ?: throw ResourceNotFoundException("Car not found with id ${humanBeing.carId}")

        if (checkNameIsNotUnique(humanBeing.name)) {
            throw ResourceAlreadyExistsException("Human with name ${humanBeing.name} already exists")
        }

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

    @Transactional
    fun updateHuman(id: Long, humanBeingDto: PutHumanBeingDto, username: String): HumanBeing? {
        val user = userService.getByUsername(username)
        val existingHumanBeing = humanBeingRepository.findById(id) ?: return null
        if (!checkOwner(user.id, existingHumanBeing.ownerId, user.role)) {
            throw OwnerPermissionsConflictException()
        }
        if (checkNameIsNotUnique(humanBeingDto.name)) {
            throw ResourceAlreadyExistsException("Human with name ${humanBeingDto.name} already exists")
        }

        val car: Car = carRepository.findById(humanBeingDto.carId)
            ?: throw ResourceNotFoundException("Car not found with id ${humanBeingDto.carId}")

        val humanBeing = humanBeingMapper.fromPutHumanBeingToEntity(
            humanBeingDto.copy(id = existingHumanBeing.id),
            existingHumanBeing.creationDate,
            car,
            user.id
        )
        logsService.humansLogsSave(HumanBeingsLogs(0, id, LogAction.UPDATED, username, LocalDateTime.now()))
        return humanBeingRepository.update(humanBeing)
    }

    @Transactional
    fun deleteHuman(humanId: Long, username: String): Boolean {
        val user = userService.getByUsername(username)
        val humanBeing = getHumanById(humanId) ?: throw ResourceNotFoundException("Human not found with id $humanId")
        if (!checkOwner(user.id, humanBeing.ownerId, user.role)) {
            throw OwnerPermissionsConflictException()
        }
        logsService.humansLogsSave(HumanBeingsLogs(0, humanId, LogAction.DELETED, username, LocalDateTime.now()))
        return humanBeingRepository.deleteById(humanId)
    }


    fun checkOwner(userId: Long, ownerId: Long, userRole: Role): Boolean {
        if (userRole == Role.ADMIN) {
            return true
        }
        return ownerId == userId
    }

    private fun checkNameIsNotUnique(name: String): Boolean {
        return name == humanBeingProperties.name && humanBeingRepository.countByName(humanBeingProperties.name) != 0L
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
