package me.sun.notificationservice.domain.entity.corona

import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion.BUSAN
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion.SEOUL
import me.sun.notificationservice.domain.entity.corona.service.KoreaCoronaStatusMessageProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

//@Disabled
@SpringBootTest
internal class KoreaCoronaStatusMessageProviderTest {

    @Autowired
    lateinit var koreaCoronaStatusMessageProvider: KoreaCoronaStatusMessageProvider

    @Test
    fun provide() {
        // when
        val coronaDailyStatusSummary = koreaCoronaStatusMessageProvider.provide(listOf(BUSAN, SEOUL))

        // then
        assertThat(coronaDailyStatusSummary.koreaCoronaStatusList.size).isEqualTo(2)
    }
}
