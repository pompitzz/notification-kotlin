package me.sun.notificationservice.domain.entity.corona.service

import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.utils.logger
import me.sun.notificationservice.domain.utils.validateEmpty
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class CoronaStatusSummaryProvider(
        private val coronaStatusQueryService: CoronaStatusQueryService
) {

    private val log = logger<CoronaStatusSummaryProvider>()

    fun provide(): CoronaStatusSummary {
        var coronaStatusList: List<CoronaStatus> = coronaStatusQueryService.findByMeasurementDate(LocalDate.now())

        if (coronaStatusList.isEmpty()) {
            log.info("### No exist today coronaStatus. start bulk coronaStatus")
            coronaStatusQueryService.bulkCoronaStatus()
            coronaStatusList = coronaStatusQueryService.findTodayOrYesterdayStatuses()
        }

        log.info("### Found {} size coronaStatuses", coronaStatusList.size)

        if (coronaStatusList.isEmpty()) throw IllegalStateException("CoronaStatusSummary must exist for today or yesterday!")

        return coronaStatusList.toCoronaStatusSummary()
    }

    private fun List<CoronaStatus>.toCoronaStatusSummary(): CoronaStatusSummary {
        validateEmpty()
        val measurementDate = first().measurementDateTime.toLocalDate()
        val totalConfirmedPersonCount = map { it.domesticOccurrenceCount + it.foreignInflowCount }.sum()
        return CoronaStatusSummary(measurementDate, totalConfirmedPersonCount, toCoronaStatusMap())
    }

    private fun List<CoronaStatus>.toCoronaStatusMap(): Map<CoronaStatusRegion, CoronaStatus> =
            groupBy { it.region }.mapValues { it.value[0] }
}

data class CoronaStatusSummary(
        val measurementDate: LocalDate,
        val totalConfirmedPersonCount: Int,
        val coronaStatusMap: Map<CoronaStatusRegion, CoronaStatus>
)
