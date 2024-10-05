package com.jellyone.lab1.service.impl

import com.jellyone.lab1.domain.logs.CarsLogs
import com.jellyone.lab1.domain.logs.HumanBeingsLogs
import com.jellyone.lab1.repository.LogsRepository
import com.jellyone.lab1.service.LogsService
import org.springframework.stereotype.Component

@Component
class LogsServiceImpl(private val logsRepository: LogsRepository) : LogsService {
    override fun humansLogsSave(log: HumanBeingsLogs) {
        logsRepository.humansLogsSave(log)
    }

    override fun carsLogsSave(log: CarsLogs) {
        logsRepository.carsLogsSave(log)
    }

}