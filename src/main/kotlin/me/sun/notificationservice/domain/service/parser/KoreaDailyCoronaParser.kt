package me.sun.notificationservice.domain.service.parser

import me.sun.notificationservice.common.URL
import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class KoreaDailyCoronaParser {
    fun parse(): KoreaCoronaStatusParseResult {
        val document = Jsoup.connect(URL.CORONA_STATUS).get()

        val content = document.getElementById("content")
        val dataTable = content.getElementsByClass("data_table")[0]
        val tableBody = dataTable.getElementsByTag("tbody")[0]

        val koreaCoronaStatusList = parseTableBody(tableBody)
        val measurementTime = parseMeasurementTime(content.selectFirst(".timetable"))
        return KoreaCoronaStatusParseResult(measurementTime, koreaCoronaStatusList)
    }

    private fun parseMeasurementTime(timeTable: Element): LocalDateTime {
        val text = timeTable.selectFirst("span").text()
        val dateTokens = text.split('.')

        if (dateTokens.size != 3) throw IllegalStateException("Invalid time table text please check. text: $text")

        val year = LocalDate.now().year
        val month = dateTokens[0].trim().toInt()
        val day = dateTokens[1].trim().toInt()
        val hour = dateTokens[2].substringBefore('시').trim().toInt()
        return LocalDateTime.of(year, month, day, hour, 0, 0)
    }

    private fun parseTableBody(tableBody: Element): List<KoreaCoronaStatus> =
            tableBody.getElementsByTag("tr")
                    .filter { !it.hasClass("sumline") }
                    .map { createKoreaCoronaStatus(it) }

    private fun createKoreaCoronaStatus(element: Element): KoreaCoronaStatus {
        val region = element.selectFirst("th").text()
        val domesticOccurrenceCount = element.selectFirst("[headers=status_level l_type2]").text().toInt()
        val foreignInflowCount = element.selectFirst("[headers=status_level l_type3]").text().toInt()
        return KoreaCoronaStatus(region, domesticOccurrenceCount, foreignInflowCount)
    }
}

data class KoreaCoronaStatusParseResult(
        val measurementTime: LocalDateTime,
        val koreaCoronaStatusList: List<KoreaCoronaStatus>
) {
    fun toEntities(): List<CoronaStatus> = koreaCoronaStatusList.map { it.toEntity(measurementTime) }
    fun isEmpty() = koreaCoronaStatusList.isEmpty()
}

data class KoreaCoronaStatus(
        val region: String,
        val domesticOccurrenceCount: Int,
        val foreignInflowCount: Int
) {
    fun toEntity(measurementTime: LocalDateTime) = CoronaStatus(
            region = CoronaStatusRegion.findByTitle(this.region),
            domesticOccurrenceCount = this.domesticOccurrenceCount,
            foreignInflowCount = this.foreignInflowCount,
            measurementDateTime = measurementTime
    )
}
