package me.sun.notificationservice.domain.service.notification

import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.entity.member.service.MemberAuthService
import me.sun.notificationservice.domain.entity.notification_evnet.CoronaEvent
import me.sun.notificationservice.domain.entity.notification_evnet.service.CoronaEventQueryService
import me.sun.notificationservice.domain.service.sender.CoronaStatusSummarySender
import me.sun.notificationservice.domain.utils.logger
import org.springframework.stereotype.Service

@Service
class CoronaEventNotificationService(
        private val coronaEventQueryService: CoronaEventQueryService,
        private val memberAuthService: MemberAuthService,
        private val coronaStatusSummarySender: CoronaStatusSummarySender
) {
    val log = logger<CoronaEventNotificationService>()

    fun notifyEvent() {
        // 토큰 재발급
        val members = coronaEventQueryService.findIsEnableEventsWithMember().map { it.member }
        if (members.isEmpty()) {
            log.info("### notify target members is zero.")
            return
        }
        memberAuthService.refreshToken(members)

        // 알림 대상 추출
        val coronaEvents = coronaEventQueryService.findIsEnableEventsWithMember()
        log.info("### Find corona event notification target. targetSize: {}", coronaEvents.size)

        // 메시지 전송
        val coronaEventNotificationDtos: List<CoronaEventNotificationDto> = coronaEvents.map { it.toEventNotificationDto() }
        coronaStatusSummarySender.send(coronaEventNotificationDtos)
        log.info("### Success send corona event. targetSize: {}", coronaEventNotificationDtos.size)
    }
}

private fun CoronaEvent.toEventNotificationDto(): CoronaEventNotificationDto {
    return CoronaEventNotificationDto(
            nickname = member.nickname,
            accessToken = member.memberToken.accessToken,
            selectRegions = regionSet.selectRegions
    )
}

data class CoronaEventNotificationDto(
        val nickname: String,
        val accessToken: String,
        val selectRegions: Set<CoronaStatusRegion>
)
