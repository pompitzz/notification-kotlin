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
    fun `must not call parser and saveAll when today daily corona exist`() {
        // given
        every { coronaStatusQueryService.findByMeasurementDate(LocalDate.now()) } returns coronaStatuses

        // when
        coronaStatusSummaryProvider.provide()

        // then
        verify(exactly = 1) { coronaStatusQueryService.findByMeasurementDate(LocalDate.now()) }
        verify(exactly = 0) { coronaStatusParser.parse() }
        verify(exactly = 0) { coronaStatusQueryService.saveAll(any()) }
        verify(exactly = 0) { coronaStatusQueryService.findTodayOrYesterdayStatuses() }
    }

    @Test
    fun `call parse and saveAll when today daily corona not exist`() {
        // given
        val parseResultMock = mockk<CoronaStatusParseResult>()
        every { parseResultMock.todayResult() } returns true
        every { parseResultMock.toEntities() } returns mockk()

        every { coronaStatusQueryService.findByMeasurementDate(LocalDate.now()) } returns emptyList()
        every { coronaStatusParser.parse() } returns parseResultMock
        every { coronaStatusQueryService.findTodayOrYesterdayStatuses() } returns coronaStatuses

        // when
        coronaStatusSummaryProvider.provide()

        // then
        verify(exactly = 1) { coronaStatusQueryService.findByMeasurementDate(LocalDate.now()) }
        verify(exactly = 1) { coronaStatusParser.parse() }
        verify(exactly = 1) { coronaStatusQueryService.saveAll(any()) }
        verify(exactly = 1) { coronaStatusQueryService.findTodayOrYesterdayStatuses() }

    }
}
