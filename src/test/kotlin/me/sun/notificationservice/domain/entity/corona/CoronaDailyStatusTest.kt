package me.sun.notificationservice.domain.entity.corona

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import me.sun.notificationservice.application.event.corona.CoronaStatusParser
import me.sun.notificationservice.application.event.corona.CoronaStatusSummaryProvider
import me.sun.notificationservice.application.event.corona.model.CoronaStatusParseResult
import me.sun.notificationservice.config.MockKTest
import me.sun.notificationservice.domain.service.corona.CoronaStatusQueryService
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

@MockKTest
internal class CoronaStatusSummaryProviderTest {

    @InjectMockKs
    lateinit var coronaStatusSummaryProvider: CoronaStatusSummaryProvider

    @RelaxedMockK
    lateinit var coronaStatusQueryService: CoronaStatusQueryService

    @RelaxedMockK
    lateinit var coronaStatusParser: CoronaStatusParser

    private val coronaStatuses = listOf(CoronaStatus(region = CoronaStatusRegion.DAEJEON, measurementDateTime = LocalDateTime.now()))

    @Test
    fun `getByMeasurementDate should not use parser when coronaStatuses by measurementDate is present`() {
        // given
        val measurementDate = LocalDate.now()
        every { coronaStatusQueryService.findByMeasurementDate(measurementDate) } returns coronaStatuses

        // when
        coronaStatusSummaryProvider.getByMeasurementDate(measurementDate)

        // then
        verify(exactly = 1) { coronaStatusQueryService.findByMeasurementDate(measurementDate) }
        verify(exactly = 0) { coronaStatusParser.parse() }
        verify(exactly = 0) { coronaStatusQueryService.saveAll(any()) }
    }

    @Test
    fun `getByMeasurementDate should use parser when coronaStatuses by measurementDate is not present`() {
        // given
        val parseResultMock = mockk<CoronaStatusParseResult>()
        every { parseResultMock.isTodayResult() } returns true
        every { parseResultMock.toEntities() } returns mockk()

        val measurementDate = LocalDate.now()
        every { coronaStatusQueryService.findByMeasurementDate(measurementDate) } returns emptyList()
        every { coronaStatusParser.parse() } returns parseResultMock
        every { coronaStatusQueryService.findTodayOrYesterdayStatuses() } returns coronaStatuses

        // when
        coronaStatusSummaryProvider.getByMeasurementDate(measurementDate)

        // then
        verify(exactly = 2) { coronaStatusQueryService.findByMeasurementDate(measurementDate) }
        verify(exactly = 1) { coronaStatusParser.parse() }
        verify(exactly = 1) { coronaStatusQueryService.saveAll(any()) }
    }
}
