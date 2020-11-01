package me.sun.notificationservice.domain.entity.notification_evnet.repo

import me.sun.notificationservice.domain.entity.notification_evnet.CoronaEvent
import org.springframework.data.jpa.repository.JpaRepository

interface CoronaEventRepository : JpaRepository<CoronaEvent, Long>, CoronaEventRepositoryCustom
