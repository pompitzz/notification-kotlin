package me.sun.notificationservice.web.controller

import me.sun.notificationservice.domain.service.notification.CoronaEventNotificationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class NotificationWebHook(
        private val coronaEventNotificationService: CoronaEventNotificationService
) {
    @GetMapping("/notify")
    fun notifyCoronaEvent() {
        coronaEventNotificationService.notifyEvent()
    }
}
