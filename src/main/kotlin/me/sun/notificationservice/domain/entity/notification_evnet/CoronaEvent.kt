package me.sun.notificationservice.domain.entity.notification_evnet

import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.entity.member.Member
import javax.persistence.*

@Entity
class CoronaEvent(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @JoinColumn(name = "member_id")
        @OneToOne(fetch = FetchType.LAZY)
        val member: Member,
        val isEnable: Boolean,
        @Convert(converter = RegionSetConverter::class)
        val regionSet: RegionSet
)

data class RegionSet(val selectRegions: Set<CoronaStatusRegion> = setOf())
