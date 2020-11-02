package me.sun.notificationservice.domain.entity.member.service

import me.sun.notificationservice.domain.entity.member.Member
import me.sun.notificationservice.domain.entity.member.repo.MemberRepository
import me.sun.notificationservice.domain.service.oauth.KakaoOAuthService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class MemberQueryService(
        private val memberRepository: MemberRepository,
        private val kakaoOAuthService: KakaoOAuthService
) {

    fun findById(id: Long) =
            memberRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("member must not null. id: $id")

    fun save(memberTokenDto: MemberTokenDto) {
        memberTokenDto.validateToken()
        val memberToken = memberTokenDto.toMemberToken()
        val (oauthId, nickName) = kakaoOAuthService.getOAuthProfile(memberToken.accessToken)
        val member = Member(oauthId = oauthId, nickname = nickName, memberToken = memberToken)
        memberRepository.save(member)
    }

    @Transactional
    fun refreshMemberToken(memberId: Long) {
        memberRepository.findAll().forEach {
            println("-------------- find all --------------")
            println(it.id)
        }
        val member = findById(memberId)
        val memberTokenDtoToUpdate = kakaoOAuthService.requestRefreshToken(member.memberToken.refreshToken)

        if (memberTokenDtoToUpdate.hasOnlyAccessToken()) {
            memberTokenDtoToUpdate.validateAccessToken()
            member.memberToken = memberTokenDtoToUpdate.updateOnlyAccessToken(member.memberToken)
        } else {
            memberTokenDtoToUpdate.validateToken()
            member.memberToken = memberTokenDtoToUpdate.toMemberToken()
        }
    }
}


