package me.sun.notificationservice.domain.entity.member

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Member(
        @Id
        @Column(name = "member_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        val oauthId: Long,
        val nickname: String,
        @Embedded
        var memberToken: MemberToken
)

@Embeddable
data class MemberToken(
        val accessToken: String,
        val accessTokenExpirationDateTime: LocalDateTime,
        val refreshToken: String,
        val refreshTokenExpirationDateTime: LocalDateTime
) {
        // 5분이하로 남았으면 재발급이 필요하다고 가정한다.
        fun needRefresh() = accessTokenExpirationDateTime.isBefore(LocalDateTime.now().plusMinutes(5))
}
