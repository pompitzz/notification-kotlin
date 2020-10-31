package me.sun.notificationservice.domain.entity.corona.service

import me.sun.notificationservice.domain.adapter.CoronaParser
import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.repo.KoreaCoronaStatusRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class KoreaCoronaStatusQueryService(
        private val coronaStatusRepository: KoreaCoronaStatusRepository,
        private val coronaParser: CoronaParser
) {
    fun bulkCoronaStatus() {
        val koreaCoronaStatusByRegionList = coronaParser.parse()
        if (koreaCoronaStatusByRegionList.isEmpty()) return

        val coronalStatusList = koreaCoronaStatusByRegionList.map { it.toEntity() }

        // TODO 동일 시간 데이터가 이미 있으면 return하기

        coronaStatusRepository.saveAll(coronalStatusList)
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
