package me.sun.notificationservice.domain.entity.member.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import me.sun.notificationservice.application.model.kakao.OAuthProfile
import me.sun.notificationservice.application.model.member.MemberTokenDto
import me.sun.notificationservice.application.oauth.KakaoOAuthService
import me.sun.notificationservice.domain.entity.member.Member
import me.sun.notificationservice.domain.entity.member.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull


@SpringBootTest
internal class MemberQueryServiceTest {
    @Autowired
    lateinit var memberQueryService: MemberQueryService

    @Autowired
    lateinit var memberRepository: MemberRepository

    @MockkBean
    lateinit var kakaoOAuthService: KakaoOAuthService

    private val baseMemberTokenDto = MemberTokenDto(
            accessToken = "123123",
            accessTokenExpiresIn = 123123,
            refreshToken = "123123",
            refreshTokenExpiresIn = 123123
    )

    @BeforeEach
    fun init() {
        memberRepository.deleteAll()
    }

    @Test
    fun save() {
        // given
        every { kakaoOAuthService.getOAuthProfile(any()) } returns OAuthProfile(10L, "nickname")

        // when
        memberQueryService.save(baseMemberTokenDto)

        // then
        val members = memberRepository.findAll()
        assertThat(members.size).isEqualTo(1)
        assertThat(members[0].nickname).isEqualTo("nickname")
        assertThat(members[0].oauthId).isEqualTo(10L)
        assertThat(members[0].memberToken.accessToken).isEqualTo(baseMemberTokenDto.accessToken)
        assertThat(members[0].memberToken.refreshToken).isEqualTo(baseMemberTokenDto.refreshToken)
    }

    @Test
    fun saveWithInvalidRefreshToken() {
        // when & then
        assertThatThrownBy {
            memberQueryService.save(MemberTokenDto(accessToken = "123123", accessTokenExpiresIn = 123123))
        }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Invalid refreshToken. refreshToken: , refreshTokenExpiresIn: 0")

    }

    @Test
    fun updateAccessAndRefreshToken() {
        // given
        val updateTokenDto = MemberTokenDto(
                accessToken = "321321",
                accessTokenExpiresIn = 1010,
                refreshToken = "321312",
                refreshTokenExpiresIn = 1010
        )

        every { kakaoOAuthService.requestTokenRefresh(any()) } returns updateTokenDto

        val savedMember = memberRepository.save(Member(oauthId = 1L, nickname = "Jayden", memberToken = baseMemberTokenDto.toMemberToken()))

        // when
        memberQueryService.refreshMemberToken(savedMember.id, updateTokenDto)

        // then
        val member = memberRepository.findByIdOrNull(savedMember.id)!!
        assertThat(member.memberToken.accessToken).isEqualTo(updateTokenDto.accessToken)
        assertThat(member.memberToken.refreshToken).isEqualTo(updateTokenDto.refreshToken)
    }

    @Test
    fun updateOnlyAccessToken() {
        // given
        val updateTokenDto = MemberTokenDto(
                accessToken = "321321",
                accessTokenExpiresIn = 1010
        )

        every { kakaoOAuthService.requestTokenRefresh(any()) } returns updateTokenDto

        memberRepository.save(Member(oauthId = 1L, nickname = "Jayden", memberToken = baseMemberTokenDto.toMemberToken()))
        // when
        memberQueryService.refreshMemberToken(1L, updateTokenDto)

        // then
        val member = memberRepository.findByIdOrNull(1L)!!
        assertThat(member.memberToken.accessToken).isEqualTo(updateTokenDto.accessToken)
        assertThat(member.memberToken.refreshToken)
                .describedAs("refresh token must be same with original refresh token")
                .isEqualTo(baseMemberTokenDto.refreshToken)
    }
}
