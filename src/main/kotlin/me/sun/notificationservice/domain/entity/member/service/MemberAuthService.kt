package me.sun.notificationservice.domain.entity.member.service

import me.sun.notificationservice.domain.entity.member.Member
import me.sun.notificationservice.domain.service.oauth.KakaoOAuthService
import me.sun.notificationservice.domain.utils.logger
import org.springframework.stereotype.Service

@Service
class MemberAuthService(
        private val memberQueryService: MemberQueryService,
        private val kakaoOAuthService: KakaoOAuthService
) {

    private val log = logger<MemberAuthService>()

    fun refreshToken(members: List<Member>) {
        val refreshTargetMembers = members.filter { it.memberToken.needRefresh() }
        log.info("### Refresh token. {} out of {}", refreshTargetMembers.size, members.size)
        refreshTargetMembers.forEach { refresh(it) }
    }

    private fun refresh(member: Member) {
        val memberTokenDto: MemberTokenDto = kakaoOAuthService.requestTokenRefresh(member.memberToken.refreshToken)
        memberQueryService.refreshMemberToken(member.id, memberTokenDto)
    }
}
