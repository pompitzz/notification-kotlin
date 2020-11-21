package me.sun.notificationservice.domain.entity.corona

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import me.sun.notificationservice.application.event.corona.CoronaStatusParser
import me.sun.notificationservice.config.MockKTest
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion.BUSAN
import me.sun.notificationservice.domain.entity.corona.repo.CoronaStatusRepository
import me.sun.notificationservice.domain.service.corona.CoronaStatusQueryService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

@MockKTest
internal class CoronaStatusQueryServiceTest {
    @InjectMockKs
    lateinit var coronaStatusQueryService: CoronaStatusQueryService

    @RelaxedMockK
    lateinit var coronaStatusRepository: CoronaStatusRepository

    @MockK
    lateinit var coronaStatusParser: CoronaStatusParser

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
