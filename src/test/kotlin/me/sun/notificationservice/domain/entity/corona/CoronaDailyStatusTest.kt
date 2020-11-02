package me.sun.notificationservice.domain.entity.corona

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import me.sun.notificationservice.config.MockKTest
import me.sun.notificationservice.domain.entity.corona.service.CoronaStatusQueryService
import me.sun.notificationservice.domain.entity.corona.service.CoronaStatusSummaryProvider
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

@MockKTest
internal class CoronaStatusSummaryProviderTest {

    @InjectMockKs
    lateinit var coronaStatusSummaryProvider: CoronaStatusSummaryProvider

    @MockK
    lateinit var coronaStatusQueryService: CoronaStatusQueryService

    private val coronaStatuses = listOf(CoronaStatus(region = CoronaStatusRegion.DAEJEON, measurementDateTime = LocalDateTime.now()))

    @Test
    fun `if today corona status is not empty, must not call bulCoronaStatus`() {
        // given
        every { coronaStatusQueryService.findByMeasurementDate(LocalDate.now()) } returns coronaStatuses

        // when
        coronaStatusSummaryProvider.provide()

        // then
        verify(exactly = 1) { coronaStatusQueryService.findByMeasurementDate(LocalDate.now()) }
        verify(exactly = 0) { coronaStatusQueryService.bulkCoronaStatus() }
        verify(exactly = 0) { coronaStatusQueryService.findTodayOrYesterdayStatuses() }
    }

    @Test
    fun `if today corona status is empty, must call bulkCoronaStatus and find today or yesterday corona status`() {
        // given
        every { coronaStatusQueryService.findByMeasurementDate(LocalDate.now()) } returns emptyList()
        every { coronaStatusQueryService.bulkCoronaStatus() } returns Unit
        every { coronaStatusQueryService.findTodayOrYesterdayStatuses() } returns coronaStatuses

        // when
        coronaStatusSummaryProvider.provide()

        // then
        verify(exactly = 1) { coronaStatusQueryService.findByMeasurementDate(LocalDate.now()) }
        verify(exactly = 1) { coronaStatusQueryService.bulkCoronaStatus() }
        verify(exactly = 1) { coronaStatusQueryService.findTodayOrYesterdayStatuses() }

    }
}
