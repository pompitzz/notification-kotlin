package me.sun.notificationservice.domain.entity.corona_evnet.repo

import com.querydsl.jpa.impl.JPAQueryFactory
import me.sun.notificationservice.domain.entity.member.QMember.member
import me.sun.notificationservice.domain.entity.corona_evnet.CoronaEvent
import me.sun.notificationservice.domain.entity.corona_evnet.QCoronaEvent.coronaEvent

class CoronaEventRepositoryImpl(private val queryFactory: JPAQueryFactory) : CoronaEventRepositoryCustom {
    override fun findIsEnableEventsWithMember(): List<CoronaEvent> =
            queryFactory
                    .selectFrom(coronaEvent)
                    .join(coronaEvent.member, member).fetchJoin()
                    .where(coronaEvent.isEnable.isTrue)
                    .fetch()
}
