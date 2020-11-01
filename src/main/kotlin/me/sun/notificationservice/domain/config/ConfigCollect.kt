package me.sun.notificationservice.domain.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import javax.persistence.EntityManager

@Configuration
class ConfigCollect {
    @Bean
    fun jpaQueryFactory(entityManager: EntityManager) = JPAQueryFactory(entityManager)

    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        val threadPoolTaskScheduler = ThreadPoolTaskScheduler()
        threadPoolTaskScheduler.poolSize = 10
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-")
        return threadPoolTaskScheduler
    }
}
