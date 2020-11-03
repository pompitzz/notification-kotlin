package me.sun.notificationservice.application.notifier

import me.sun.notificationservice.application.model.corona.CoronaEventNotificationDto
import me.sun.notificationservice.domain.entity.member.service.MemberAuthService
import me.sun.notificationservice.domain.entity.corona_evnet.service.CoronaEventQueryService
import me.sun.notificationservice.application.sender.CoronaStatusSummarySender
import me.sun.notificationservice.common.utils.logger
import me.sun.notificationservice.domain.entity.corona_evnet.CoronaEvent
import org.springframework.stereotype.Service

@Service
class CoronaEventNotifier(
        private val coronaEventQueryService: CoronaEventQueryService,
        private val memberAuthService: MemberAuthService,
        private val coronaStatusSummarySender: CoronaStatusSummarySender
) {
    val log = logger<CoronaEventNotifier>()

    fun notifyEvent() {
        refreshToken()
        val coronaEvents = extractEvents()
        sendMessage(coronaEvents)
    }

    private fun refreshToken() {
        val members = coronaEventQueryService.findIsEnableEventsWithMember().map { it.member }
        memberAuthService.refreshToken(members)
    }

    private fun extractEvents(): List<CoronaEvent> {
        val coronaEvents = coronaEventQueryService.findIsEnableEventsWithMember()
        log.info("### Find corona event notification target. targetSize: {}", coronaEvents.size)
        return coronaEvents
    }

    private fun sendMessage(coronaEvents: List<CoronaEvent>) {
        if (coronaEvents.isEmpty()) {
            log.info("### corona event notification target is zero")
            return
        }

        val coronaEventNotificationDtos = coronaEvents.map { CoronaEventNotificationDto.from(it) }
        coronaStatusSummarySender.send(coronaEventNotificationDtos)
        log.info("### Success send corona event. notification target size: {}", coronaEventNotificationDtos.size)
    }
}
