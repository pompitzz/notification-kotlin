package me.sun.notificationservice.domain.sender

import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.entity.corona.service.KoreaCoronaStatus
import me.sun.notificationservice.domain.entity.corona.service.KoreaCoronaStatusMessageProvider
import me.sun.notificationservice.domain.entity.corona.service.KoreaCoronaStatusSummary
import me.sun.notificationservice.domain.sender.model.KakaoMsgContent
import me.sun.notificationservice.domain.sender.model.KakaoMsgListType
import org.springframework.stereotype.Component

@Component
class CoronaStatusSummarySender(
        private val koreaCoronaStatusMessageProvider: KoreaCoronaStatusMessageProvider,
        private val kakaoMessageSender: KakaoMessageSender
) {
    fun send(memberDto: MemberDto) {
        val coronaStatusSummary: KoreaCoronaStatusSummary = koreaCoronaStatusMessageProvider.provide(memberDto.selectRegions)
        kakaoMessageSender.send(coronaStatusSummary.toKakaoMsgListType(), memberDto.accessToken)
    }

    private fun KoreaCoronaStatusSummary.toKakaoMsgListType() =
            KakaoMsgListType(
                    headerTitle = "국내 코로나 상황($measurementDate)",
                    contents = mutableListOf(KakaoMsgContent("총 확진자: $totalConfirmedPersonCount"))
                            .apply { addAll(koreaCoronaStatusList.toKakaoMsgContents()) }
            )

    private fun List<KoreaCoronaStatus>.toKakaoMsgContents() =
            map { KakaoMsgContent("${it.name}: ${it.totalConfirmedPersonCount}") }
}

data class MemberDto(
        val accessToken: String,
        val selectRegions: List<CoronaStatusRegion>
)
