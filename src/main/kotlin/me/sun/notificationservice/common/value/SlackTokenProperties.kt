package me.sun.notificationservice.common.value

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "slack.token")
class SlackTokenProperties {
    lateinit var bot: String
    lateinit var user: String
}
