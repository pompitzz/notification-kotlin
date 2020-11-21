package me.sun.notificationservice.common.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Collection<*>?.validateEmpty() {
    if (this == null || this.isEmpty()) {
        throw IllegalStateException("Current collection must not be empty.")
    }
}

fun LocalDate.toMonthDay(): String = this.format(DateTimeFormatter.ofPattern("MM-dd"))
fun LocalDateTime.isToday(): Boolean = this.toLocalDate().isEqual(LocalDate.now())
fun LocalDateTime.toSeoulEpochSecond(): Long = this.toEpochSecond(ZoneOffset.ofHours(9))

// private val log = logger<ClassName>()
inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)
