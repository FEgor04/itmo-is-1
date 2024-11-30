package com.jellyone.lab1.service.impl

import com.jellyone.lab1.domain.logs.CarsLogs
import com.jellyone.lab1.domain.logs.HumanBeingsLogs
import com.jellyone.lab1.repository.LogsRepository
import com.jellyone.lab1.service.LogsService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class LogsServiceImpl(private val logsRepository: LogsRepository) : LogsService {
    @Transactional
    override fun humansLogsSave(log: HumanBeingsLogs) {
        logsRepository.humansLogsSave(log)
    }

    @Transactional
    override fun carsLogsSave(log: CarsLogs) {
        logsRepository.carsLogsSave(log)
    }

}