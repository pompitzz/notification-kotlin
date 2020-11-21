package me.sun.notificationservice.domain.service.corona_event

import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.entity.corona_evnet.CoronaEvent
import me.sun.notificationservice.domain.entity.corona_evnet.RegionSet
import me.sun.notificationservice.domain.entity.corona_evnet.repo.CoronaEventRepository
import me.sun.notificationservice.domain.entity.member.Member
import me.sun.notificationservice.domain.service.member.MemberQueryService
import org.springframework.stereotype.Service

@Service
class CoronaEventQueryService(
        private val coronaEventRepository: CoronaEventRepository,
        private val memberQueryService: MemberQueryService
) {
    fun save(eventDto: EventDto) {
        val member = memberQueryService.findById(eventDto.memberId)
        val coronaEvent = eventDto.toEntity(member)
        coronaEventRepository.save(coronaEvent)
    }

    fun findIsEnableEventsWithMember() = coronaEventRepository.findIsEnableEventsWithMember()
}

data class EventDto(
        val memberId: Long,
        val isEnable: Boolean = true,
        val regionSet: Set<CoronaStatusRegion>
) {
    fun toEntity(member: Member) = CoronaEvent(member = member, isEnable = isEnable, regionSet = RegionSet(regionSet))
}
