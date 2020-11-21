package me.sun.notificationservice.application.event.corona

import me.sun.notificationservice.application.event.corona.model.CoronaStatusSummary
import me.sun.notificationservice.common.extension.logger
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
class CoronaEventNotifier(
        private val coronaStatusSummaryProvider: CoronaStatusSummaryProvider,
        private val coronaStatusSummarySender: CoronaStatusSummarySender
) {

    private val log = logger<CoronaStatusSummarySender>()

    companion object {
        val SINGLE_EXECUTOR: ExecutorService = Executors.newFixedThreadPool(1)
    }

    fun notifyEvent() {
        val coronaStatusSummary: CoronaStatusSummary = coronaStatusSummaryProvider.provide()

        if (isNotSummaryForToday(coronaStatusSummary)) {
            log.info("[Retry Corona Alert] current summary is not for today. retry after 10 minute")
            retryAfterMinute(5)
            return
        }

        coronaStatusSummary.logging()
        try {
            coronaStatusSummarySender.send(coronaStatusSummary)
        } catch (e: Exception) {
            log.error("Fail corona status summary send retry after 1 minute]", e)
            retryAfterMinute(1)
        }
    }

    private fun isNotSummaryForToday(coronaStatusSummary: CoronaStatusSummary): Boolean {
        return LocalDate.now().isAfter(coronaStatusSummary.measurementDate)
    }

    private fun retryAfterMinute(minute: Long) {
        SINGLE_EXECUTOR.execute {
            TimeUnit.MINUTES.sleep(minute)
            notifyEvent()
        }
    }

    private fun CoronaStatusSummary.logging() {
        log.info("### Get coronaStatusSummary. measurementDate: {} totalConfirmedPersonCount: {}", measurementDate, totalConfirmedPersonCount)
    }
}
