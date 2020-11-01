package me.sun.notificationservice.domain.service.adapter

import me.sun.notificationservice.domain.service.parser.KoreaDailyCoronaParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KoreaDailyCoronaParserTest {
    private val coronaParser = KoreaDailyCoronaParser()

    @Test
    fun `when parsing, 합계 must be filtered`() {
        // when
        val koreaCoronaStatusByRegionList = coronaParser.parse()

        // then
        koreaCoronaStatusByRegionList.koreaCoronaStatusList.forEach {
            assertThat(it.region).isNotEqualTo("합계")
        }
    }
}
