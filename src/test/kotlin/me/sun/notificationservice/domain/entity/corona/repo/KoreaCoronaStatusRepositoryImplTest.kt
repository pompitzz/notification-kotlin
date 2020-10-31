package me.sun.notificationservice.domain.entity.corona.repo

import me.sun.notificationservice.config.ImportQueryDsl
import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion.BUSAN
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDate
import java.time.LocalDateTime

@ImportQueryDsl
@DataJpaTest
internal class KoreaCoronaStatusRepositoryImplTest {
    @Autowired
    lateinit var coronaStatusRepository: KoreaCoronaStatusRepository

    @BeforeEach
    fun init() {
        val now = LocalDateTime.now()
        val coronaStatusList = listOf(
                CoronaStatus(region = BUSAN, measurementDateTime = now.minusDays(1)),
                CoronaStatus(region = BUSAN, measurementDateTime = now),
                CoronaStatus(region = BUSAN, measurementDateTime = now.plusDays(1))
        )
        coronaStatusRepository.saveAll(coronaStatusList)
    }

    @Test
    fun findByMeasurementTime() {
        // when
        val todayCoronaStatusList = coronaStatusRepository.findByMeasurementDate(LocalDate.now())

        // then
        assertThat(todayCoronaStatusList.size).isEqualTo(1)
        assertThat(todayCoronaStatusList[0].measurementDateTime.toLocalDate()).isEqualTo(LocalDate.now())
    }

    @Test
    fun countByMeasurementTime() {
        // when
        val todayCount = coronaStatusRepository.countByMeasurementDate(LocalDate.now())

        // then
        assertThat(todayCount).isEqualTo(1)
    }
}
