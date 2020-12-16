package me.sun.notificationservice.common.value

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class SlackTokenPropertiesTest {
    @Autowired
    lateinit var slackTokenProperties: SlackTokenProperties

    @Test
    fun `properties test`() {
        assertThat(slackTokenProperties.bot).isEqualTo("123")
        assertThat(slackTokenProperties.user).isEqualTo("456")
    }
}
