package me.sun.notificationservice.domain.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException

fun Collection<*>?.validateEmpty() {
    if (this == null || this.isEmpty()) {
        throw IllegalStateException("Current collection must not be empty.")
    }
}

inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)
