package me.sun.notificationservice.domain.entity.corona

import me.sun.notificationservice.domain.entity.corona.service.CoronaStatusSummary
import me.sun.notificationservice.domain.entity.corona.service.CoronaStatusSummaryProvider
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Disabled
@SpringBootTest
internal class CoronaStatusSummaryProviderTest {

    @Autowired
    lateinit var coronaStatusSummaryProvider: CoronaStatusSummaryProvider

    @Test
    fun provide() {
        // when
        val summary: CoronaStatusSummary = coronaStatusSummaryProvider.provide()

        // then
        println("date: ${summary.measurementDate} (${summary.totalConfirmedPersonCount})")
        summary.coronaStatusMap.forEach { (_, value) -> println("${value.region}: ${value.domesticOccurrenceCount}, ${value.foreignInflowCount}") }
    }
}
