package me.sun.notificationservice.domain.adapter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CoronaParserTest {
    private val coronaParser = CoronaParser()

    @Test
    fun `when parsing, 합계 must be filtered`() {
        // when
        val koreaCoronaStatusByRegionList = coronaParser.parse()

        // then
        koreaCoronaStatusByRegionList.forEach {
            assertThat(it.region).isNotEqualTo("합계")

        }
    }
}
