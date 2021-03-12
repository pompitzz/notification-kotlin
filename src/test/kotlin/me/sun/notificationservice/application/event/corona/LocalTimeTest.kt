package me.sun.notificationservice.application.event.corona

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalTime

internal class LocalTimeTest {
    @Test
    fun `LocalTime compare`() {
        assertThat(LocalTime.of(11, 59) > LocalTime.of(12, 0)).isFalse()
        assertThat(LocalTime.of(12, 0) > LocalTime.of(12, 0)).isFalse()
        assertThat(LocalTime.of(12, 1) > LocalTime.of(12, 0)).isTrue()
    }
}
