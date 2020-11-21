package me.sun.notificationservice.domain.service.corona

import me.sun.notificationservice.common.extension.logger
import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.repo.CoronaStatusRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CoronaStatusQueryService(
        private val coronaStatusRepository: CoronaStatusRepository
) {
    private val log = logger<CoronaStatusQueryService>()

    fun saveAll(coronaStatusList: List<CoronaStatus>) {
        if (coronaStatusList.isEmpty()) {
            log.warn("coronaStatusList is Empty")
            return
        }
        coronaStatusRepository.saveAll(coronaStatusList)
    }

    fun findTodayOrYesterdayStatuses(): List<CoronaStatus> {
        val today = LocalDate.now()
        if (isExistByMeasurementDate(today)) {
            return coronaStatusRepository.findByMeasurementDate(today)
        }

        val yesterday = today.minusDays(1)
        return coronaStatusRepository.findByMeasurementDate(yesterday)
    }

    fun findByMeasurementDate(measurementDate: LocalDate): List<CoronaStatus> {
        measurementDate.validateMeasurementDay()
        return coronaStatusRepository.findByMeasurementDate(measurementDate)
    }

    fun isExistByMeasurementDate(measurementDate: LocalDate): Boolean {
        measurementDate.validateMeasurementDay()
        return coronaStatusRepository.countByMeasurementDate(measurementDate) > 0
    }
}

private fun LocalDate.validateMeasurementDay() {
    if (this.isAfter(LocalDate.now())) {
        throw IllegalArgumentException("measurementDay must not be after today. but is $this")
    }
}
