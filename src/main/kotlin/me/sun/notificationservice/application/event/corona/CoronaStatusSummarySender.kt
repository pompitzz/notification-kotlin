package me.sun.notificationservice.application.event.corona

import me.sun.notificationservice.application.event.corona.model.CoronaStatusSummary
import me.sun.notificationservice.application.sender.slack.SlackMessageSender
import me.sun.notificationservice.application.sender.slack.model.SlackAttachment
import me.sun.notificationservice.application.sender.slack.model.SlackAttachmentField
import me.sun.notificationservice.common.SLACK_CHANNEL
import me.sun.notificationservice.common.URL
import me.sun.notificationservice.common.extension.logger
import me.sun.notificationservice.common.extension.toMonthDay
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import org.springframework.stereotype.Component

@Component
class CoronaStatusSummarySender(
        private val slackMessageSender: SlackMessageSender
) {

    private val log = logger<CoronaStatusSummarySender>()

    fun send(coronaStatusSummary: CoronaStatusSummary) {
        log.info("### Start send corona status summary -> measurementDate: {} totalConfirmedPersonCount: {}",
                coronaStatusSummary.measurementDate, coronaStatusSummary.totalConfirmedPersonCount)
        val slackAttachment = coronaStatusSummary.toSlackAttachment()
        slackMessageSender.send(SLACK_CHANNEL.CORONA_NOTIFICATION, slackAttachment)
        slackMessageSender.send(SLACK_CHANNEL.PRIVATE_CORONA_NOTIFICATION, slackAttachment)
    }

    private fun CoronaStatusSummary.toSlackAttachment(): SlackAttachment {
        val title = "국내 코로나 상황(${measurementDate.toMonthDay()})"

        val top5Summary = coronaStatusMap.values
                .sortedByDescending { it.sumCount() }
                .filter { it.sumCount() != 0 }
//            .filterIndexed { index, _ -> index < 5 }
                .joinToString { it.toSummary() }

        val selectSummary = listOf(CoronaStatusRegion.SEOUL, CoronaStatusRegion.BUSAN)
                .mapNotNull { coronaStatusMap[it] }
                .sortedByDescending { it.sumCount() }
                .joinToString { it.toSummary() }

        val fields = listOf(
                SlackAttachmentField("총 확진자: $totalConfirmedPersonCount"),
                SlackAttachmentField("모든 지역", top5Summary),
                SlackAttachmentField("선택한 지역", selectSummary)
        )

        return SlackAttachment(
                title = title,
                title_link = URL.CORONA_STATUS,
                footer = "코로나 알리미",
                fields = fields
        )
    }
}
