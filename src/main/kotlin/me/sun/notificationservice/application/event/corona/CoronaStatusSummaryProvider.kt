package me.sun.notificationservice.application.event.corona

import me.sun.notificationservice.application.event.corona.model.CoronaStatusDto
import me.sun.notificationservice.application.event.corona.model.CoronaStatusParseResult
import me.sun.notificationservice.application.event.corona.model.CoronaStatusSummary
import me.sun.notificationservice.common.extension.logger
import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.service.corona.CoronaStatusQueryService
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class CoronaStatusSummaryProvider(
        private val coronaStatusParser: CoronaStatusParser,
        private val coronaStatusQueryService: CoronaStatusQueryService
) {

    private val log = logger<CoronaStatusSummaryProvider>()

    fun getByMeasurementDate(measurementDate: LocalDate): CoronaStatusSummary? {
        var coronaStatusList: List<CoronaStatus> = coronaStatusQueryService.findByMeasurementDate(measurementDate)

        if (coronaStatusList.isEmpty()) {
            log.info("### No exist today corona status list. Try load today corona status list")
            parseCoronaStatus()
            coronaStatusList = coronaStatusQueryService.findByMeasurementDate(measurementDate)
        }

        log.info("### Found {} size coronaStatuses", coronaStatusList.size)

        return coronaStatusList.toCoronaStatusSummary()
    }

    private fun parseCoronaStatus() {
        val coronaStatusParseResult: CoronaStatusParseResult = coronaStatusParser.parse()
        if (coronaStatusParseResult.isTodayResult()) {
            coronaStatusQueryService.saveAll(coronaStatusParseResult.toEntities())
        }
    }

    private fun List<CoronaStatus>.toCoronaStatusSummary(): CoronaStatusSummary? {
        if (this.isEmpty()) return null
        val measurementDate = first().measurementDateTime.toLocalDate()
        val totalConfirmedPersonCount = map { it.domesticOccurrenceCount + it.foreignInflowCount }.sum()
        return CoronaStatusSummary(measurementDate, totalConfirmedPersonCount, toCoronaStatusMap())
    }

    // region으로 group by 후 첫번째 값들만 dto로 변환하여 mapping 한다.(요구 사항에 따라 group by 된 값은 하나만 존재해야한다.)
    private fun List<CoronaStatus>.toCoronaStatusMap(): Map<CoronaStatusRegion, CoronaStatusDto> =
            groupBy { it.region }.mapValues { CoronaStatusDto.from(it.value[0]) }
}
