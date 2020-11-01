package me.sun.notificationservice.domain.entity.corona.repo

import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import java.time.LocalDate

interface CoronaStatusRepositoryCustom {
    fun findByMeasurementDate(measurementDate: LocalDate): List<CoronaStatus>
    fun countByMeasurementDate(measurementDate: LocalDate): Long
}
