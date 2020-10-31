package me.sun.notificationservice.domain.sender

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
        coronaStatusSummarySender.send(MemberDto(
                "jEKGw84a0xIixqQS8aejKhdFTvCvSI81hKHS7gopyNoAAAF1f2GsPA",
                listOf(CoronaStatusRegion.BUSAN, CoronaStatusRegion.SEOUL)
        ))
    }
}
