package me.sun.notificationservice.domain.service.member

import me.sun.notificationservice.application.oauth.KakaoOAuthService
import me.sun.notificationservice.application.oauth.model.MemberTokenDto
import me.sun.notificationservice.domain.entity.member.Member
import me.sun.notificationservice.domain.entity.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class MemberQueryService(
        private val memberRepository: MemberRepository,
        private val kakaoOAuthService: KakaoOAuthService
) {

    fun findById(id: Long?) =
            memberRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("member must not null. id: $id")

    fun save(memberTokenDto: MemberTokenDto) {
        memberTokenDto.validateToken()
        val memberToken = memberTokenDto.toMemberToken()
        val (oauthId, nickName) = kakaoOAuthService.getOAuthProfile(memberToken.accessToken)
        val member = Member(oauthId = oauthId, nickname = nickName, memberToken = memberToken)
        memberRepository.save(member)
    }

    @Transactional
    fun refreshMemberToken(memberId: Long?, memberTokenDto: MemberTokenDto): Member {
        val member = findById(memberId)
        if (memberTokenDto.hasOnlyAccessToken()) {
            memberTokenDto.validateAccessToken()
            member.memberToken = memberTokenDto.updateOnlyAccessToken(member.memberToken)
        } else {
            memberTokenDto.validateToken()
            member.memberToken = memberTokenDto.toMemberToken()
        }
        return member
    }
}


