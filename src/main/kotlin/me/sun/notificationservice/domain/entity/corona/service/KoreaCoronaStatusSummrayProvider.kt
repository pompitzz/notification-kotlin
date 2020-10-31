package me.sun.notificationservice.domain.entity.corona.service

import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.utils.logger
import me.sun.notificationservice.domain.utils.validateEmpty
import org.springframework.stereotype.Component
import java.lang.IllegalStateException
import java.time.LocalDate

@Component
class KoreaCoronaStatusMessageProvider(
        private val koreaCoronaStatusQueryService: KoreaCoronaStatusQueryService
) {

    private val log = logger<KoreaCoronaStatusMessageProvider>()

    fun provide(regions: List<CoronaStatusRegion>): KoreaCoronaStatusSummary {
        koreaCoronaStatusQueryService.bulkCoronaStatus()
        val coronaStatusList = koreaCoronaStatusQueryService.findTodayOrYesterdayStatuses()
        return coronaStatusList.buildMessageModel(regions)
    }

    private fun List<CoronaStatus>.buildMessageModel(regions: List<CoronaStatusRegion>): KoreaCoronaStatusSummary {
        this.validateEmpty()
        val measurementDate = this.validateSameDaysAndGetSameDay()
        val totalConfirmedPersonCount = this.map { it.domesticOccurrenceCount + it.foreignInflowCount }.sum()
        val coronalDailyStatusListBySelectedRegions = this.buildByInterestRegions(regions)
        return KoreaCoronaStatusSummary(measurementDate, totalConfirmedPersonCount, coronalDailyStatusListBySelectedRegions)
    }

    private fun List<CoronaStatus>.buildByInterestRegions(regions: List<CoronaStatusRegion>): List<KoreaCoronaStatus> {
        val coronaStatusGroupByRegion = this.groupBy { it.region }
                .mapValues {
                    if (it.value.size > 1) {
                        log.warn("coronaStatusList size must be 1 when group by region but current is ${it.value} by ${it.key}")
                    }
                    it.value[0]
                }
        return regions
                .mapNotNull { coronaStatusGroupByRegion[it] }
                .map { KoreaCoronaStatus(it.region.title, it.domesticOccurrenceCount + it.foreignInflowCount) }
    }

    private fun List<CoronaStatus>.validateSameDaysAndGetSameDay(): LocalDate {
        val measurementDateTimeSet = this.map { it.measurementDateTime }.toSet()
        if (measurementDateTimeSet.size > 1) {
            throw IllegalStateException("Each data time of coronalStatusList must be same. But current: $measurementDateTimeSet")
        }
        return measurementDateTimeSet.first().toLocalDate()
    }
}

data class KoreaCoronaStatusSummary(
        val measurementDate: LocalDate,
        val totalConfirmedPersonCount: Int,
        val koreaCoronaStatusList: List<KoreaCoronaStatus>
)

data class KoreaCoronaStatus(
        val name: String,
        val totalConfirmedPersonCount: Int = 0
)
