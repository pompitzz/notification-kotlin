package me.sun.notificationservice.application.corona

import me.sun.notificationservice.common.utils.logger
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CoronaEventScheduler(
        private val coronaEventNotifier: CoronaEventNotifier
) {

    private val log = logger<CoronaEventScheduler>()

    @Async
    @Scheduled(cron = "0 10 10 * * *") // 매일 오전 10시 10분 0초에 수행
    fun run() {
        log.info("### start corona event notification")
        coronaEventNotifier.notifyEvent()
    }
}
