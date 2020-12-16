package me.sun.notificationservice.application.sender.slack

import com.fasterxml.jackson.databind.ObjectMapper
import me.sun.notificationservice.application.adapter.JsonRequestInfo
import me.sun.notificationservice.application.adapter.RestTemplateAdapter
import me.sun.notificationservice.application.exception.MessageSendException
import me.sun.notificationservice.application.sender.slack.model.SlackAttachment
import me.sun.notificationservice.application.sender.slack.model.SlackMessageDto
import me.sun.notificationservice.common.URL
import me.sun.notificationservice.common.extension.logger
import me.sun.notificationservice.common.value.SlackTokenProperties
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


const val MAX_TRY_COUNT = 5

val SENDER_EXECUTOR: ExecutorService = Executors.newFixedThreadPool(10)

@Component
class SlackMessageSender(
        private val objectMapper: ObjectMapper,
        private val slackTokenProperties: SlackTokenProperties
) {

    private val log = logger<SlackMessageSender>()

    fun send(channel: String, attachment: SlackAttachment) {
        val slackMessageDto = SlackMessageDto(channel, listOf(attachment))

        val sender: () -> ResponseEntity<String> = {
            RestTemplateAdapter.requestWithJson(
                    JsonRequestInfo(
                            accessToken = slackTokenProperties.bot,
                            json = objectMapper.writeValueAsString(slackMessageDto),
                            requestUrl = URL.SLACK_SEND_MESSAGE_URL,
                            requestMethod = HttpMethod.POST
                    )
            )
        }
        CompletableFuture.runAsync(Runnable { sendWithRetry(sender, 1, 10) }, SENDER_EXECUTOR)
    }

    fun sendWithRetry(sender: () -> Any, retryCount: Int, intervalSeconds: Long) {
        try {
            sender.invoke()
        } catch (e: Exception) {
            if (retryCount > MAX_TRY_COUNT) {
                log.warn("[Fail Slack Message Send]. exceed MAX_TRY_COUNT({}).", MAX_TRY_COUNT, e)
                throw MessageSendException("Fail Slack Message Send", e)
            }
            log.info("[Fail Slack Message Send] start retry with sleep {} seconds... currentRetryCount: {}", intervalSeconds, retryCount)
            TimeUnit.SECONDS.sleep(intervalSeconds)
            sendWithRetry(sender, retryCount + 1, intervalSeconds)
        }
    }
}
