package me.sun.notificationservice.application.sender.slack

import me.sun.notificationservice.application.sender.ExceptionMessageSender
import me.sun.notificationservice.application.sender.slack.model.SlackAttachment
import me.sun.notificationservice.application.sender.slack.model.SlackAttachmentField
import me.sun.notificationservice.common.SLACK_CHANNEL
import org.springframework.stereotype.Component

@Component
class SlackExceptionMessageSender(
        private val slackMessageSender: SlackMessageSender
) : ExceptionMessageSender {

    override fun send(e: Exception) {
        send(e, "")
    }

    override fun send(e: Exception, title: String) {
        val slackAttachment = toSlackAttachment(e, title)
        slackMessageSender.send(SLACK_CHANNEL.PRIVATE_CORONA_NOTIFICATION, slackAttachment)
    }

    private fun toSlackAttachment(e: Exception, message: String): SlackAttachment {
        return SlackAttachment(
                title = if (message.isBlank()) "에러가 발생하였습니다" else message,
                fields = e.toSlackFields(),
                footer = "Error Alert"
        )
    }

    private fun Exception.toSlackFields(): List<SlackAttachmentField> {
        return listOf(
                SlackAttachmentField(
                        value = message
                )
        )
    }
}
