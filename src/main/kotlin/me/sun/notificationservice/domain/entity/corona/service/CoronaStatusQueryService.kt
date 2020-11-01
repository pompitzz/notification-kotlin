package me.sun.notificationservice.domain.entity.corona.service

import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.repo.CoronaStatusRepository
import me.sun.notificationservice.domain.service.parser.KoreaCoronaStatusParseResult
import me.sun.notificationservice.domain.service.parser.KoreaDailyCoronaParser
import me.sun.notificationservice.domain.utils.logger
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CoronaStatusQueryService(
        private val coronaStatusRepository: CoronaStatusRepository,
        private val koreaDailyCoronaParser: KoreaDailyCoronaParser
) {
    private val log = logger<CoronaStatusQueryService>()

    fun bulkCoronaStatus() {
        val koreaCoronaStatusParseResult: KoreaCoronaStatusParseResult = koreaDailyCoronaParser.parse()

        if (koreaCoronaStatusParseResult.isEmpty()) {
            log.warn("koreaCoronaStatusParseResult is Empty")
            return
        }
        val coronalStatusList = koreaCoronaStatusParseResult.toEntities()

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
        throw IllegalArgumentException("measurementDay must not be after today.")
    }
}
