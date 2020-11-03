package me.sun.notificationservice.application.sender

import me.sun.notificationservice.application.model.corona.CoronaEventNotificationDto
import me.sun.notificationservice.application.model.corona.CoronaStatusSummary
import me.sun.notificationservice.application.model.kakao.KakaoMsgContent
import me.sun.notificationservice.application.model.kakao.KakaoMsgListType
import me.sun.notificationservice.application.model.kakao.KakaoMsgTextType
import me.sun.notificationservice.application.provider.CoronaStatusSummaryProvider
import me.sun.notificationservice.common.URL
import me.sun.notificationservice.common.utils.logger
import me.sun.notificationservice.common.utils.toMonthDay
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import org.springframework.stereotype.Component

private val CORONA_STATUS_LINK = KakaoMsgTextType(text = "자세히(${URL.CORONA_STATUS})")

@Component
class CoronaStatusSummarySender(
        private val coronaStatusSummaryProvider: CoronaStatusSummaryProvider,
        private val kakaoMessageSender: KakaoMessageSender
) {
    private val log = logger<CoronaStatusSummarySender>()

    fun send(coronaEventNotificationDtos: List<CoronaEventNotificationDto>) {
        val coronaStatusSummary: CoronaStatusSummary = coronaStatusSummaryProvider.provide()
        coronaStatusSummary.logging()
        coronaEventNotificationDtos.forEach { send(it, coronaStatusSummary) }
    }

    fun send(coronaEventNotificationDto: CoronaEventNotificationDto, coronaStatusSummary: CoronaStatusSummary) {
        val accessToken = coronaEventNotificationDto.accessToken
        val toKakaoMsgListType = coronaStatusSummary.toKakaoMsgListType(coronaEventNotificationDto.selectRegions)
        sendCoronaStatusSummary(toKakaoMsgListType, accessToken)
        sendCoronaStatusLink(accessToken)
    }

    private fun sendCoronaStatusSummary(kakaoMegListType: KakaoMsgListType, accessToken: String) {
        kakaoMessageSender.send(kakaoMegListType, accessToken)
    }

    private fun sendCoronaStatusLink(accessToken: String) {
        kakaoMessageSender.send(CORONA_STATUS_LINK, accessToken)
    }

    private fun CoronaStatusSummary.logging() {
        log.info("### Get coronaStatusSummary. measurementDate: {} totalConfirmedPersonCount: {}", measurementDate, totalConfirmedPersonCount)
    }
}

private fun CoronaStatusSummary.toKakaoMsgListType(selectRegions: Set<CoronaStatusRegion>): KakaoMsgListType {
    val kakaoMsgContents = mutableListOf(KakaoMsgContent("총 확진자: $totalConfirmedPersonCount"))
    val title = selectRegions.mapNotNull { coronaStatusMap[it] }
            .sortedByDescending { it.domesticOccurrenceCount + it.foreignInflowCount }
            .joinToString { "${it.regionTitle}: ${it.domesticOccurrenceCount + it.foreignInflowCount}" }
    kakaoMsgContents.add(KakaoMsgContent(title))
    return KakaoMsgListType(headerTitle = "국내 코로나 상황(${measurementDate.toMonthDay()})", contents = kakaoMsgContents)
}