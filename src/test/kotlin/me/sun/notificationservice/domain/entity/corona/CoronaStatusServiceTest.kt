package me.sun.notificationservice.domain.entity.corona

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import me.sun.notificationservice.config.MockKTest
import me.sun.notificationservice.domain.adapter.CoronaParser
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@MockKTest
internal class CoronaStatusServiceTest {
    @InjectMockKs
    lateinit var coronaStatusService: CoronaStatusService

    @RelaxedMockK
    lateinit var coronaStatusRepository: CoronaStatusRepository

    @MockK
    lateinit var coronaParser: CoronaParser

    @Test
    fun `bulk when parser return empty list should not call saveAll`() {
        // given
        every { coronaParser.parse() } returns emptyList()

        // when
        coronaStatusService.bulkKoreaCoronaStatus()

        // then
        verify(exactly = 1) { coronaParser.parse() }
        verify(exactly = 0) { coronaStatusRepository.saveAll(emptyList()) }
    }
}

@Disabled
@SpringBootTest
internal class CoronaStatusServiceIntegTest {
    @Autowired
    lateinit var coronaStatusService: CoronaStatusService

    @Test
    fun bulkTest() {
        coronaStatusService.bulkKoreaCoronaStatus()
        coronaStatusService.findAll().forEach { println(it) }
    }
}
