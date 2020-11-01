package me.sun.notificationservice.domain.entity.notification_evnet.repo

import me.sun.notificationservice.domain.entity.notification_evnet.CoronaEvent

interface CoronaEventRepositoryCustom {
    fun findIsEnableEventsWithMember(): List<CoronaEvent>
}
