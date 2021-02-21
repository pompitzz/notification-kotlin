package me.sun.notificationservice.web.controller

import me.sun.notificationservice.application.event.corona.CoronaEventNotifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CoronaEventController(
        private val coronaSimpleEventNotifier: CoronaEventNotifier
) {
    @GetMapping("/notify")
    fun notifyCoronaEvent() = if (coronaSimpleEventNotifier.notifyEvent()) "success" else "fail"
}
