package me.sun.notificationservice.application.sender.slack

import com.fasterxml.jackson.databind.ObjectMapper
import me.sun.notificationservice.application.adapter.JsonRequestInfo
import me.sun.notificationservice.application.adapter.RestTemplateAdapter
import me.sun.notificationservice.application.sender.slack.model.SlackAttachment
import me.sun.notificationservice.application.sender.slack.model.SlackMessageDto
import me.sun.notificationservice.common.URL
import me.sun.notificationservice.common.value.TokenValueProvider
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component

@Component
class SlackMessageSender(
        private val objectMapper: ObjectMapper,
        private val tokenValueProvider: TokenValueProvider
) {
    fun send(channel: String, attachment: SlackAttachment) {
        val slackMessageDto = SlackMessageDto(channel, listOf(attachment))
        val response = RestTemplateAdapter.requestWithJson<String>(
                JsonRequestInfo(
                        accessToken = tokenValueProvider.slackBotToken,
                        json = objectMapper.writeValueAsString(slackMessageDto),
                        requestUrl = URL.SLACK_SEND_MESSAGE_URL,
                        requestMethod = HttpMethod.POST
                )
        )
    }
}
