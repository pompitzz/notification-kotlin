package me.sun.notificationservice.domain.service.notification

import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import me.sun.notificationservice.domain.entity.member.Member
import me.sun.notificationservice.domain.entity.member.MemberToken
import me.sun.notificationservice.domain.entity.member.repo.MemberRepository
import me.sun.notificationservice.domain.entity.notification_evnet.CoronaEvent
import me.sun.notificationservice.domain.entity.notification_evnet.RegionSet
import me.sun.notificationservice.domain.entity.notification_evnet.repo.CoronaEventRepository
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@Disabled
@SpringBootTest
internal class CoronaEventNotificationServiceTest {
    @Autowired
    lateinit var coronaEventNotificationService: CoronaEventNotificationService

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var coronaEventRepository: CoronaEventRepository

    @Test
    fun notifyEvent() {
        // given
        val token = MemberToken(
                accessToken = "5XTQbHsqisDlGxdi7L3kr1Uei0GQXT6Ig7mB7QorDR4AAAF1gsil0g",
                accessTokenExpirationDateTime = LocalDateTime.now(),
                refreshToken = "QVqBlnheA5fyFH6l5vglm5vhBc7XnZjfshG7pQorDR4AAAF1gsil0A",
                refreshTokenExpirationDateTime = LocalDateTime.now()
        )
        val member1 = Member(1L, 1L, "member1", token)
        memberRepository.save(member1)

        val event1 = CoronaEvent(member = member1, isEnable = true, regionSet = RegionSet(setOf(CoronaStatusRegion.SEOUL, CoronaStatusRegion.BUSAN)))
        coronaEventRepository.save(event1)

        // when
        coronaEventNotificationService.notifyEvent()
    }
}