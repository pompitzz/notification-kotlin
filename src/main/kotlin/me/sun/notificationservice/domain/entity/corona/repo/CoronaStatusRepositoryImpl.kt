package me.sun.notificationservice.domain.entity.corona.repo

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import me.sun.notificationservice.domain.entity.corona.CoronaStatus
import me.sun.notificationservice.domain.entity.corona.QCoronaStatus.coronaStatus
import java.time.LocalDate

class CoronaStatusRepositoryImpl(private val queryFactory: JPAQueryFactory) : CoronaStatusRepositoryCustom {
    override fun findByMeasurementDate(measurementDate: LocalDate): List<CoronaStatus> =
            queryFactory
                    .selectFrom(coronaStatus)
                    .where(isMeasurementDay(measurementDate))
                    .fetch()

    override fun countByMeasurementDate(measurementDate: LocalDate): Long =
            queryFactory
                    .selectFrom(coronaStatus)
                    .where(isMeasurementDay(measurementDate))
                    .fetchCount()

    private fun isMeasurementDay(measurementDay: LocalDate): BooleanExpression {
        val measurementDateTime = coronaStatus.measurementDateTime
        return measurementDateTime.year().eq(measurementDay.year)
                .and(measurementDateTime.month().eq(measurementDay.monthValue))
                .and(measurementDateTime.dayOfMonth().eq(measurementDay.dayOfMonth))
    }
}
