package me.sun.notificationservice.domain.entity.corona.service

import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.utils.validateEmpty
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class CoronaStatusSummaryProvider(
        private val coronaStatusQueryService: CoronaStatusQueryService
) {

    fun provide(): CoronaStatusSummary {
        var coronaStatusList: List<CoronaStatus> = coronaStatusQueryService.findByMeasurementDate(LocalDate.now())

        if (coronaStatusList.isEmpty()) {
            coronaStatusQueryService.bulkCoronaStatus()
            coronaStatusList = coronaStatusQueryService.findTodayOrYesterdayStatuses()
        }

        if (coronaStatusList.isEmpty()) throw IllegalStateException("CoronaStatusSummary must exist for today or yesterday!")

        return coronaStatusList.toCoronaStatusSummary()
    }

    private fun List<CoronaStatus>.toCoronaStatusSummary(): CoronaStatusSummary {
        validateEmpty()
        val measurementDate = validateSameDaysAndGetSameDay()
        val totalConfirmedPersonCount = map { it.domesticOccurrenceCount + it.foreignInflowCount }.sum()
        return CoronaStatusSummary(measurementDate, totalConfirmedPersonCount, toCoronaStatusMap())
    }

    private fun List<CoronaStatus>.toCoronaStatusMap(): Map<CoronaStatusRegion, CoronaStatus> =
            groupBy { it.region }.mapValues { it.value[0] }

    private fun List<CoronaStatus>.validateSameDaysAndGetSameDay(): LocalDate {
        val measurementDateTimeSet = this.map { it.measurementDateTime }.toSet()
        if (measurementDateTimeSet.size > 1) {
            throw IllegalStateException("Each data time of coronalStatusList must be same. But current: $measurementDateTimeSet")
        }
        return measurementDateTimeSet.first().toLocalDate()
    }
}

data class CoronaStatusSummary(
        val measurementDate: LocalDate,
        val totalConfirmedPersonCount: Int,
        val coronaStatusMap: Map<CoronaStatusRegion, CoronaStatus>
)
