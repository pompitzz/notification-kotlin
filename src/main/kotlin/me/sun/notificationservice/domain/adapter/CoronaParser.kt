package me.sun.notificationservice.domain.adapter

import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

const val URL = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13&ncvContSeq=&contSeq=&board_id=&gubun="

@Component
class CoronaParser {
    fun parse(): List<KoreaCoronaStatusByRegion> {
        val document = Jsoup.connect(URL).get()
        val content = document.getElementById("content")
        val measuringTime = parseMeasuringTime(content.selectFirst(".timetable"))

        val dataTable = content.getElementsByClass("data_table")[0]

        val tableBody = dataTable.getElementsByTag("tbody")[0]

        return parseTableBody(tableBody, measuringTime)
    }

    private fun parseTableBody(tableBody: Element, measuringTime: LocalDateTime) =
            tableBody.getElementsByTag("tr")
                    .filter { !it.hasClass("subline") }
                    .map { createData(it, measuringTime) }

    private fun parseMeasuringTime(timeTable: Element): LocalDateTime {
        val text = timeTable.selectFirst("span").text()
        val dateTokens = text.split('.')

        if (dateTokens.size != 3) throw IllegalStateException("timeTable의 text가 유효하지 않습니다. text: $text")

        val year = LocalDate.now().year
        val month = dateTokens[0].trim().toInt()
        val day = dateTokens[1].trim().toInt()
        val hour = dateTokens[2].substringBefore('시').trim().toInt()
        return LocalDateTime.of(year, month, day, hour, 0, 0)
    }

    private fun createData(element: Element, measuringTime: LocalDateTime): KoreaCoronaStatusByRegion {
        val region = element.selectFirst("th").text()
        val domesticOccurrenceCount = element.selectFirst("[headers=status_level l_type1]").text().toInt()
        val foreignInflowCount = element.selectFirst("[headers=status_level l_type3]").text().toInt()
        return KoreaCoronaStatusByRegion(region, domesticOccurrenceCount, foreignInflowCount, measuringTime)
    }
}

data class KoreaCoronaStatusByRegion(
        val region: String,
        val domesticOccurrenceCount: Int,
        val foreignInflowCount: Int,
        val measuringTime: LocalDateTime
) {
    fun toEntity() = CoronaStatus(
            region = this.region,
            domesticOccurrenceCount = this.domesticOccurrenceCount,
            foreignInflowCount = this.foreignInflowCount,
            measuringTime = this.measuringTime
    )
}
