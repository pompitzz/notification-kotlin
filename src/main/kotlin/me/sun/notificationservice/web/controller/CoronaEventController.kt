package me.sun.notificationservice.web.controller

import me.sun.notificationservice.application.corona.CoronaEventNotifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CoronaEventController(
        private val coronaEventNotifier: CoronaEventNotifier
) {
    @GetMapping("/notify")
    fun notifyCoronaEvent() {
        coronaEventNotifier.notifyEvent()
    }
}
