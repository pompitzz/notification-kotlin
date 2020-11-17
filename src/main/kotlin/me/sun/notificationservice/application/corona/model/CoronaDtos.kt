package me.sun.notificationservice.application.corona.model

import me.sun.notificationservice.common.utils.isToday
import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.entity.corona_evnet.CoronaEvent
import java.time.LocalDate
import java.time.LocalDateTime

data class CoronaStatusParseResult(
        val coronaStatusDtoList: List<CoronaStatusDto>
) {
    fun toEntities(): List<CoronaStatus> = coronaStatusDtoList.map { it.toEntity() }

    fun todayResult() = coronaStatusDtoList.isNotEmpty() && coronaStatusDtoList[0].measurementDateTime.isToday()
}

data class CoronaStatusSummary(
        val measurementDate: LocalDate,
        val totalConfirmedPersonCount: Int,
        val coronaStatusMap: Map<CoronaStatusRegion, CoronaStatusDto>
)

data class CoronaStatusDto(
        val regionTitle: String,
        val domesticOccurrenceCount: Int,
        val foreignInflowCount: Int,
        val measurementDateTime: LocalDateTime
) {
    companion object {
        fun from(coronaStatus: CoronaStatus) = with(coronaStatus) {
            CoronaStatusDto(region.title, domesticOccurrenceCount, foreignInflowCount, measurementDateTime)
        }
    }

    fun toEntity() = CoronaStatus(
            region = CoronaStatusRegion.findByTitle(this.regionTitle),
            domesticOccurrenceCount = this.domesticOccurrenceCount,
            foreignInflowCount = this.foreignInflowCount,
            measurementDateTime = this.measurementDateTime
    )

    fun sumCount() = domesticOccurrenceCount + foreignInflowCount
    fun toSummary() = "${regionTitle}: ${domesticOccurrenceCount + foreignInflowCount}"
}

data class CoronaEventNotificationDto(
        val nickname: String,
        val accessToken: String,
        val selectRegions: Set<CoronaStatusRegion>
) {
    companion object {
        fun from(coronaEvent: CoronaEvent) = with(coronaEvent) {
            CoronaEventNotificationDto(member.nickname, member.memberToken.accessToken, regionSet.selectRegions)
        }
    }
}
