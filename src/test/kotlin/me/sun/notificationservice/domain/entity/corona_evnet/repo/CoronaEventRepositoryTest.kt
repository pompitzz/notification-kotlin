package me.sun.notificationservice.domain.entity.corona_evnet.repo

import me.sun.notificationservice.config.DataJpaTestWithQueryDsl
import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion.*
import me.sun.notificationservice.domain.entity.member.Member
import me.sun.notificationservice.domain.entity.member.MemberToken
import me.sun.notificationservice.domain.entity.member.MemberRepository
import me.sun.notificationservice.domain.entity.corona_evnet.CoronaEvent
import me.sun.notificationservice.domain.entity.corona_evnet.RegionSet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@DataJpaTestWithQueryDsl
internal class CoronaEventRepositoryTest {

    @Autowired
    lateinit var coronaEventRepository: CoronaEventRepository

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun findAllWithMember() {
        // given
        val token = MemberToken(
                accessToken = "123123",
                accessTokenExpirationDateTime = LocalDateTime.now(),
                refreshToken = "123123",
                refreshTokenExpirationDateTime = LocalDateTime.now()
        )
        val member1 = memberRepository.save(Member(1L, 1L, "member1", token))
        val member2 = memberRepository.save(Member(2L, 2L, "member2", token))
        val member3 = memberRepository.save(Member(3L, 3L, "member2", token))

        val event1 = CoronaEvent(member = member1, isEnable = true, regionSet = RegionSet(setOf(SEOUL, BUSAN)))
        val event2 = CoronaEvent(member = member2, isEnable = false, regionSet = RegionSet(setOf(BUSAN, GYEONGGI)))
        val event3 = CoronaEvent(member = member3, isEnable = true, regionSet = RegionSet(setOf(ULSAN, DAEJEON)))
        coronaEventRepository.save(event1)
        coronaEventRepository.save(event2)
        coronaEventRepository.save(event3)

        // when
        println("=========== start =============")
        val coronaEventList = coronaEventRepository.findIsEnableEventsWithMember()

        // then
        assertThat(coronaEventList.size).isEqualTo(2)
        assertThat(coronaEventList).extracting("id").containsOnly(1L, 3L)
    }
}
