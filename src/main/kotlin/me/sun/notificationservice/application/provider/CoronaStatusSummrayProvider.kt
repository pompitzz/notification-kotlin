package me.sun.notificationservice.application.provider

import me.sun.notificationservice.application.model.corona.CoronaStatusDto
import me.sun.notificationservice.application.parser.CoronaStatusParser
import me.sun.notificationservice.application.model.corona.CoronaStatusParseResult
import me.sun.notificationservice.application.model.corona.CoronaStatusSummary
import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.common.utils.logger
import me.sun.notificationservice.common.utils.validateEmpty
import me.sun.notificationservice.domain.entity.corona.service.CoronaStatusQueryService
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class CoronaStatusSummaryProvider(
        private val coronaStatusParser: CoronaStatusParser,
        private val coronaStatusQueryService: CoronaStatusQueryService
) {

    private val log = logger<CoronaStatusSummaryProvider>()

    fun provide(): CoronaStatusSummary {
        var coronaStatusList: List<CoronaStatus> = coronaStatusQueryService.findByMeasurementDate(LocalDate.now())

        if (coronaStatusList.isEmpty()) {
            val coronaStatusParseResult: CoronaStatusParseResult = coronaStatusParser.parse()
            coronaStatusQueryService.saveAll(coronaStatusParseResult.toEntities())
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

    // region으로 group by 후 첫번째 값들만 dto로 변환하여 mapping 한다.(요구 사항에 따라 group by 된 값은 하나만 존재해야한다.)
    private fun List<CoronaStatus>.toCoronaStatusMap(): Map<CoronaStatusRegion, CoronaStatusDto> =
            groupBy { it.region }.mapValues { CoronaStatusDto.from(it.value[0]) }
}
