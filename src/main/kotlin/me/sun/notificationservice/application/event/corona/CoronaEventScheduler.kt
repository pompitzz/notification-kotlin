package me.sun.notificationservice.application.event.corona

import me.sun.notificationservice.common.extension.logger
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CoronaEventScheduler(
        private val coronaRetryEventNotifier: CoronaEventNotifier
) {

    private val log = logger<CoronaEventScheduler>()

    @Async
    @Scheduled(cron = "0 30 9 * * *") // 매일 오전 7시에 수행
    fun run() {
        log.info("### start corona event notification")
        coronaRetryEventNotifier.notifyEvent()
    }
}
