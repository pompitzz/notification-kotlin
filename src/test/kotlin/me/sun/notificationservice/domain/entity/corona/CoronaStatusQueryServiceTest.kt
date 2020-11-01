package me.sun.notificationservice.domain.entity.corona

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import me.sun.notificationservice.config.MockKTest
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion.BUSAN
import me.sun.notificationservice.domain.entity.corona.repo.CoronaStatusRepository
import me.sun.notificationservice.domain.entity.corona.service.CoronaStatusQueryService
import me.sun.notificationservice.domain.service.parser.KoreaCoronaStatusParseResult
import me.sun.notificationservice.domain.service.parser.KoreaDailyCoronaParser
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime

@MockKTest
internal class CoronaStatusQueryServiceTest {
    @InjectMockKs
    lateinit var coronaStatusQueryService: CoronaStatusQueryService

    @RelaxedMockK
    lateinit var coronaStatusRepository: CoronaStatusRepository

    @MockK
    lateinit var koreaDailyCoronaParser: KoreaDailyCoronaParser

    @Test
    fun `bulk when parser return empty list, should not call saveAll`() {
        // given
        every { koreaDailyCoronaParser.parse() } returns KoreaCoronaStatusParseResult(LocalDateTime.now(), emptyList())

        // when
        coronaStatusQueryService.bulkCoronaStatus()

        // then
        verify(exactly = 1) { koreaDailyCoronaParser.parse() }
        verify(exactly = 0) { coronaStatusRepository.saveAll(emptyList()) }
    }

    @Test
    fun `"findByMeasurementDate" when measurementDate is after today, should throw exception`() {
        // given
        val tomorrow = LocalDate.now().plusDays(1)

        // when && then
        assertThatThrownBy { coronaStatusQueryService.findByMeasurementDate(tomorrow) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("measurementDay must not be after today. but is $tomorrow")
    }

    @Test
    fun `"findByMeasurementDate" when measurementDate is today, should call repository`() {
        // given
        val today = LocalDate.now()
        val stubData = listOf(
                CoronaStatus(region = BUSAN, measurementDateTime = LocalDateTime.now())
        )
        every { coronaStatusRepository.findByMeasurementDate(today) } returns stubData

        // when
        val coronaStatusList = coronaStatusQueryService.findByMeasurementDate(today)

        // then
        assertThat(coronaStatusList).isEqualTo(stubData)
        verify(exactly = 1) { coronaStatusRepository.findByMeasurementDate(today) }
    }

    @Test
    fun `"findTodayOrYesterday" when exist today data, should return today data`() {
        // given
        val today = LocalDate.now()
        val stubData = listOf(
                CoronaStatus(region = BUSAN, measurementDateTime = LocalDateTime.now())
        )
        every { coronaStatusRepository.countByMeasurementDate(today) } returns 1
        every { coronaStatusRepository.findByMeasurementDate(today) } returns stubData

        // when
        val coronaStatusList = coronaStatusQueryService.findTodayOrYesterdayStatuses()

        // then
        assertThat(coronaStatusList).isEqualTo(stubData)
        verify(exactly = 1) { coronaStatusRepository.countByMeasurementDate(any()) }
        verify(exactly = 1) { coronaStatusRepository.findByMeasurementDate(any()) }
    }

    @Test
    fun `"findTodayOrYesterday" when exist only yesterday data, should return yesterday data`() {
        // given
        val yesterday = LocalDate.now().minusDays(1)
        val stubData = listOf(
                CoronaStatus(region = BUSAN, measurementDateTime = LocalDateTime.now())
        )
        every { coronaStatusRepository.countByMeasurementDate(yesterday) } returns 1
        every { coronaStatusRepository.findByMeasurementDate(yesterday) } returns stubData

        // when
        val coronaStatusList = coronaStatusQueryService.findTodayOrYesterdayStatuses()

        // then
        assertThat(coronaStatusList).isEqualTo(stubData)
        verify(exactly = 1) { coronaStatusRepository.countByMeasurementDate(any()) }
        verify(exactly = 1) { coronaStatusRepository.findByMeasurementDate(any()) }
    }
}

@SpringBootTest
internal class CoronaStatusQueryServiceIntegTest {
    @Autowired
    lateinit var coronaStatusQueryService: CoronaStatusQueryService

    @Test
    fun bulkTest() {
        coronaStatusQueryService.bulkCoronaStatus()
        coronaStatusQueryService.findByMeasurementDate(LocalDate.now().minusDays(1))
                .forEach {
                    with(it) {
                        println("CoronaStatus(id=$id, region='$region', domesticOccurrenceCount=$domesticOccurrenceCount, foreignInflowCount=$foreignInflowCount, measurementTime=$measurementDateTime)")
                    }
                }
    }
}
