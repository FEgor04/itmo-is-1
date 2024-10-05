package com.jellyone.lab1.service

import com.jellyone.lab1.domain.logs.CarsLogs
import com.jellyone.lab1.domain.logs.HumanBeingsLogs

interface LogsService {
    fun humansLogsSave(log: HumanBeingsLogs)
    fun carsLogsSave(log: CarsLogs)
}