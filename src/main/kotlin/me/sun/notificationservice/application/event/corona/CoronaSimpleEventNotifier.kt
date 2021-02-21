package me.sun.notificationservice.application.event.corona

import me.sun.notificationservice.application.event.corona.model.CoronaStatusSummary
import me.sun.notificationservice.common.extension.logger
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CoronaSimpleEventNotifier(
        private val coronaStatusSummaryProvider: CoronaStatusSummaryProvider,
        private val coronaStatusSummarySender: CoronaStatusSummarySender
) : CoronaEventNotifier {

    private val log = logger<CoronaStatusSummarySender>()

    override fun notifyEvent(): Boolean {
        try {
            val coronaStatusSummary: CoronaStatusSummary =
                    coronaStatusSummaryProvider.getByMeasurementDate(LocalDate.now()) ?: return false

            coronaStatusSummarySender.sendSuccess(coronaStatusSummary)
            return true
        } catch (e: Exception) {
            log.error("Fail Corona Simple Event Notifier.", e)
            return false
        }
    }
}
