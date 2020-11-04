package me.sun.notificationservice.common.value

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TokenValueProvider(
        @Value("\${slack.token.bot}")
        val slackBotToken: String,
        @Value("\${slack.token.user}")
        val slackUserToken: String
)
