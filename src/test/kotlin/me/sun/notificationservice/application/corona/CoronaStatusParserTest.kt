package me.sun.notificationservice.application.corona

import me.sun.notificationservice.application.event.corona.CoronaStatusParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CoronaStatusParserTest {
    private val coronaParser = CoronaStatusParser()

    @Test
    fun `when parsing, 합계 must be filtered`() {
        // when
        val koreaCoronaStatusByRegionList = coronaParser.parse()

        // then
        koreaCoronaStatusByRegionList.coronaStatusDtoList.forEach {
            assertThat(it.regionTitle).isNotEqualTo("합계")
        }
    }
}
