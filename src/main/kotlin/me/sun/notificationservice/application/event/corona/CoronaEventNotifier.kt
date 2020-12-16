package me.sun.notificationservice.application.event.corona

import me.sun.notificationservice.application.event.corona.model.CoronaStatusSummary
import me.sun.notificationservice.application.sender.ExceptionMessageSender
import me.sun.notificationservice.common.extension.logger
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
class CoronaEventNotifier(
        private val coronaStatusSummaryProvider: CoronaStatusSummaryProvider,
        private val coronaStatusSummarySender: CoronaStatusSummarySender,
        private val exceptionMessageSender: ExceptionMessageSender
) {

    private val log = logger<CoronaStatusSummarySender>()

    companion object {
        val SINGLE_EXECUTOR: ExecutorService = Executors.newFixedThreadPool(1)
    }

    fun notifyEvent() {
        try {
            val coronaStatusSummary: CoronaStatusSummary? = coronaStatusSummaryProvider.getByMeasurementDate(LocalDate.now())

            if (coronaStatusSummary == null) {
                log.info("[Retry Corona Alert] current summary is not for today. start retry with sleep 5 minute ...")
                TimeUnit.MINUTES.sleep(5)
                notifyEvent()
                return
            }

            coronaStatusSummarySender.send(coronaStatusSummary)
        } catch (e: Exception) {
            exceptionMessageSender.send(e, "CoronaEventNotifier Fail!")
        }
    }
}
