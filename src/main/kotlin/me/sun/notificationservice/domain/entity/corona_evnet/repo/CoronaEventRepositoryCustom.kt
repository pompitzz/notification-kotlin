package me.sun.notificationservice.domain.entity.corona_evnet.repo

import me.sun.notificationservice.domain.entity.corona_evnet.CoronaEvent

interface CoronaEventRepositoryCustom {
    fun findIsEnableEventsWithMember(): List<CoronaEvent>
}
