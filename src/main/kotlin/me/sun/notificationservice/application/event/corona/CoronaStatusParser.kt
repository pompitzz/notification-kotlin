package me.sun.notificationservice.application.event.corona

import me.sun.notificationservice.application.event.corona.model.CoronaStatusDto
import me.sun.notificationservice.application.event.corona.model.CoronaStatusParseResult
import me.sun.notificationservice.application.exception.ParserFailException
import me.sun.notificationservice.common.URL
import me.sun.notificationservice.common.extension.logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

const val MAX_TRY_COUNT = 10

@Component
class CoronaStatusParser {

    private val log = logger<CoronaStatusParser>()

    fun parse() = parseWithRetry(1)

    private fun parseWithRetry(retryCount: Int): CoronaStatusParseResult {
        try {
            val document = Jsoup.connect(URL.CORONA_STATUS).get()

            val content = document.getElementById("content")
            val dataTable = content.getElementsByClass("data_table")[0]
            val tableBody = dataTable.getElementsByTag("tbody")[0]

            val measurementDateTime = parseMeasurementTime(content.selectFirst(".timetable"))
            val koreaCoronaStatusList = parseTableBody(tableBody, measurementDateTime)

            return CoronaStatusParseResult(koreaCoronaStatusList)
        } catch (e: Exception) {
            if (MAX_TRY_COUNT > retryCount) {
                log.warn("[Fail Parse Corona Status]. exceed MAX_TRY_COUNT({}).", MAX_TRY_COUNT, e)
                throw ParserFailException("Fail Parse Corona Status MAX_TRY_COUNT($MAX_TRY_COUNT)", e)
            }

            log.info("[Fail Parse Corona Status] start retry with sleep 5 minutes... currentRetryCount: {}", retryCount)
            TimeUnit.SECONDS.sleep(5)
            return parseWithRetry(retryCount + 1)
        }
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

    private fun parseTableBody(tableBody: Element, measurementDateTime: LocalDateTime): List<CoronaStatusDto> =
            tableBody.getElementsByTag("tr")
                    .filter { !it.hasClass("sumline") }
                    .map { buildDto(it, measurementDateTime) }

    private fun buildDto(element: Element, measurementDateTime: LocalDateTime): CoronaStatusDto {
        val region = element.selectFirst("th").text()
        val domesticOccurrenceCount = element.selectFirst("[headers=status_level l_type2]").text().toInt()
        val foreignInflowCount = element.selectFirst("[headers=status_level l_type3]").text().toInt()
        return CoronaStatusDto(region, domesticOccurrenceCount, foreignInflowCount, measurementDateTime)
    }
}
