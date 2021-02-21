package me.sun.notificationservice.application.sender.slack.model

import me.sun.notificationservice.common.extension.toSeoulEpochSecond
import java.time.LocalDateTime

const val LOUD_SPEACKER_ICON_URL = "https://a.slack-edge.com/production-standard-emoji-assets/10.2/apple-large/1f4e2@2x.png"

data class SlackAttachment(
        val color: String = "#36a64f",
        val pretext: String? = null,
        val authorName: String? = null,
        val authorLink: String? = null,
        val author_icon: String? = null,
        val title: String? = null,
        val title_link: String? = null,
        val text: String? = null,
        val fields: List<SlackAttachmentField>,
        val imageUrl: String? = null,
        val thumbUrl: String? = null,
        val footer: String? = null,
        val footer_icon: String = LOUD_SPEACKER_ICON_URL,
        val ts: Long = LocalDateTime.now().toSeoulEpochSecond()
)

data class SlackAttachmentField(
        val title: String? = null,
        val value: String? = null,
        val short: Boolean = false
)

data class SlackMessageDto(
        val channel: String,
        val attachments: List<SlackAttachment>
)
