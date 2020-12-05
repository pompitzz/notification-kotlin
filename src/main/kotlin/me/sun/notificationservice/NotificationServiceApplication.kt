package me.sun.notificationservice

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAsync
@EnableScheduling
@SpringBootApplication
class NotificationServiceApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(NotificationServiceApplication::class.java)
            .web(WebApplicationType.SERVLET)
            .run(*args)
}
