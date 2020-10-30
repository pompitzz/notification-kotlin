package me.sun.notificationservice.domain.entity.corona

import me.sun.notificationservice.domain.adapter.CoronaParser
import org.springframework.stereotype.Service

@Service
class CoronaStatusService(
        private val coronaStatusRepository: CoronaStatusRepository,
        private val coronaParser: CoronaParser
) {
    fun bulkKoreaCoronaStatus() {
        val koreaCoronaStatusByRegionList = coronaParser.parse()
        if (koreaCoronaStatusByRegionList.isEmpty()) return

        val coronalStatusList = koreaCoronaStatusByRegionList.map { it.toEntity() }

        // TODO 동일 시간 데이터가 이미 있으면 return하기

        coronaStatusRepository.saveAll(coronalStatusList)
    }

    fun findAll(): MutableList<CoronaStatus> = coronaStatusRepository.findAll()
}
