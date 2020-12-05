package me.sun.notificationservice.common.value

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "slack.token")
class SlackTokenProperties {
    lateinit var bot: String
    lateinit var user: String
}
