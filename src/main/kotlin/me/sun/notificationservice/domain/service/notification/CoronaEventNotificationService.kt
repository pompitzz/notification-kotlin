package me.sun.notificationservice.domain.service.notification

import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.entity.member.Member
import me.sun.notificationservice.domain.entity.member.service.MemberQueryService
import me.sun.notificationservice.domain.entity.notification_evnet.CoronaEvent
import me.sun.notificationservice.domain.entity.notification_evnet.service.CoronaEventQueryService
import me.sun.notificationservice.domain.service.sender.CoronaStatusSummarySender
import me.sun.notificationservice.domain.utils.logger
import org.springframework.stereotype.Service

@Service
class CoronaEventNotificationService(
        private val coronaEventQueryService: CoronaEventQueryService,
        private val memberQueryService: MemberQueryService,
        private val coronaStatusSummarySender: CoronaStatusSummarySender
) {
    val log = logger<CoronaEventNotificationService>()

    fun notifyEvent() {
        // 알림 대상 추출
        val coronaEvents = coronaEventQueryService.findIsEnableEventsWithMember()
        log.info("### Find corona event notification target. targetSize: {}", coronaEvents.size)

        // 토큰 재발급
        refreshTokenIfNeed(coronaEvents.map { it.member })

        // 메시지 전송
        val coronaEventNotificationDtos: List<CoronaEventNotificationDto> = coronaEvents.map { it.toEventNotificationDto() }
        coronaStatusSummarySender.send(coronaEventNotificationDtos)
        log.info("### Success send corona event. targetSize: {}", coronaEventNotificationDtos.size)
    }

    // TODO 토큰 재발급은 책임을 분리하자
    private fun refreshTokenIfNeed(members: List<Member>) {
        val membersNeedTokenRefresh = members.filter { it.memberToken.needRefresh() }
        log.info("### Request token refresh to notify event. {} out of {}", membersNeedTokenRefresh.size, members.size)
        membersNeedTokenRefresh.forEach { memberQueryService.refreshMemberToken(it.id!!) }
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
