package me.sun.notificationservice.domain.entity.member.repo

import me.sun.notificationservice.config.DataJpaTestWithQueryDsl
import me.sun.notificationservice.domain.entity.member.Member
import me.sun.notificationservice.domain.entity.member.MemberRepository
import me.sun.notificationservice.domain.entity.member.MemberToken
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@DataJpaTestWithQueryDsl
internal class MemberRepositoryTest {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun updateTokenTest() {
        val firstToken = MemberToken(
                accessToken = "123123",
                accessTokenExpirationDateTime = LocalDateTime.now(),
                refreshToken = "123123",
                refreshTokenExpirationDateTime = LocalDateTime.now()
        )

        memberRepository.save(Member(oauthId = 1L, nickname = "Jayden", memberToken = firstToken))

        val firstSavedMember = memberRepository.findByIdOrNull(1L)!!
        assertThat(firstSavedMember.memberToken).isEqualTo(firstToken)

        val secondToken = MemberToken(
                accessToken = "321321",
                accessTokenExpirationDateTime = LocalDateTime.now(),
                refreshToken = "321321",
                refreshTokenExpirationDateTime = LocalDateTime.now()
        )
        firstSavedMember.memberToken = secondToken
        memberRepository.save(firstSavedMember)

        val secondSavedMember = memberRepository.findByIdOrNull(1L)!!
        assertThat(secondSavedMember.memberToken).isEqualTo(secondToken)
        assertThat(memberRepository.count()).isEqualTo(1L)
    }
}
