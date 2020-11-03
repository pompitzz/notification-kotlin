package me.sun.notificationservice.application.sender

import me.sun.notificationservice.application.model.corona.CoronaEventNotificationDto
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Disabled
@SpringBootTest
internal class CoronaStatusSummarySenderTest {
    @Autowired
    lateinit var coronaStatusSummarySender: CoronaStatusSummarySender

    @Test
    fun send() {
        coronaStatusSummarySender.send(
                listOf(
                        CoronaEventNotificationDto("1", "_7ecotJRpF-RcJqkUZqLB2S6fDe4VCwgrrKb3AopcNEAAAF1gs11QQ", setOf(CoronaStatusRegion.BUSAN, CoronaStatusRegion.SEOUL)),
                        CoronaEventNotificationDto("1", "_7ecotJRpF-RcJqkUZqLB2S6fDe4VCwgrrKb3AopcNEAAAF1gs11QQ", emptySet())
                ))
    }
}
