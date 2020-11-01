package me.sun.notificationservice.domain.service.sender

import me.sun.notificationservice.common.URL
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.entity.corona.service.CoronaStatusSummary
import me.sun.notificationservice.domain.entity.corona.service.CoronaStatusSummaryProvider
import me.sun.notificationservice.domain.service.notification.CoronaEventNotificationDto
import me.sun.notificationservice.domain.service.sender.model.KakaoMsgContent
import me.sun.notificationservice.domain.service.sender.model.KakaoMsgListType
import me.sun.notificationservice.domain.service.sender.model.KakaoMsgTextType
import me.sun.notificationservice.domain.utils.toMonthDay
import org.springframework.stereotype.Component

private val CORONA_STATUS_LINK = KakaoMsgTextType(text = "자세히(${URL.CORONA_STATUS})")

@Component
class CoronaStatusSummarySender(
        private val coronaStatusSummaryProvider: CoronaStatusSummaryProvider,
        private val kakaoMessageSender: KakaoMessageSender
) {
    fun send(coronaEventNotificationDtos: List<CoronaEventNotificationDto>) {
        val coronaStatusSummary: CoronaStatusSummary = coronaStatusSummaryProvider.provide()
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
}

private fun CoronaStatusSummary.toKakaoMsgListType(selectRegions: Set<CoronaStatusRegion>): KakaoMsgListType {
    val kakaoMsgContents = mutableListOf(KakaoMsgContent("총 확진자: $totalConfirmedPersonCount"))
    val title = selectRegions.mapNotNull { coronaStatusMap[it] }
            .sortedByDescending { it.domesticOccurrenceCount + it.foreignInflowCount }
            .joinToString { "${it.region.title}: ${it.domesticOccurrenceCount + it.foreignInflowCount}" }
    kakaoMsgContents.add(KakaoMsgContent(title))
    return KakaoMsgListType(headerTitle = "국내 코로나 상황(${measurementDate.toMonthDay()})", contents = kakaoMsgContents)
}
