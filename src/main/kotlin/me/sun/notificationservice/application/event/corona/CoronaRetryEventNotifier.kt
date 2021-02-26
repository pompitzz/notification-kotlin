package me.sun.notificationservice.application.event.corona

import me.sun.notificationservice.application.event.corona.model.CoronaStatusSummary
import me.sun.notificationservice.application.sender.ExceptionMessageSender
import me.sun.notificationservice.common.extension.logger
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@Service
class CoronaRetryEventNotifier(
        private val coronaStatusSummaryProvider: CoronaStatusSummaryProvider,
        private val coronaStatusSummarySender: CoronaStatusSummarySender,
        private val exceptionMessageSender: ExceptionMessageSender
) : CoronaEventNotifier {

    private val log = logger<CoronaStatusSummarySender>()

    override fun notifyEvent() = doNotify(0)

    fun doNotify(retryCount: Int): Boolean {
        try {
            val coronaStatusSummary: CoronaStatusSummary? = coronaStatusSummaryProvider.getByMeasurementDate(LocalDate.now())

            if (coronaStatusSummary == null) {
                log.info("[Retry Corona Alert] current summary is not for today. retry after sleeping 1 minute...")
                if (retryCount > 200) {
                    if (retryCount % 100 == 0) {
                        coronaStatusSummarySender.sendRetry(retryCount, "<@dongmyeong.lee22>")
                    }
                } else if (retryCount % 200 == 0) {
                    coronaStatusSummarySender.sendRetry(retryCount, "<@dongmyeong.lee22>")
                }
                TimeUnit.MINUTES.sleep(1)
                return doNotify(retryCount + 1)
            }

            coronaStatusSummarySender.sendSuccess(coronaStatusSummary)
            return true
        } catch (e: Exception) {
            exceptionMessageSender.send(e, "CoronaEventNotifier Fail!")
            return false
        }
    }
}
