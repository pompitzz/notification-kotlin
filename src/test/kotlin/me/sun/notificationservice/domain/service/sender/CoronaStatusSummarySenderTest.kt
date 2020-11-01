package me.sun.notificationservice.domain.service.sender

import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class CoronaStatusSummarySenderTest {
    @Autowired
    lateinit var coronaStatusSummarySender: CoronaStatusSummarySender

    @Test
    fun send() {
        coronaStatusSummarySender.send(
                listOf(
                        MemberDto("_7ecotJRpF-RcJqkUZqLB2S6fDe4VCwgrrKb3AopcNEAAAF1gs11QQ", listOf(CoronaStatusRegion.BUSAN, CoronaStatusRegion.SEOUL)),
                        MemberDto("_7ecotJRpF-RcJqkUZqLB2S6fDe4VCwgrrKb3AopcNEAAAF1gs11QQ", emptyList())
                ))
    }
}
