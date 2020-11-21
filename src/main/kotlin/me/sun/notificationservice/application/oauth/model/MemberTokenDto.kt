package me.sun.notificationservice.application.oauth.model

import com.fasterxml.jackson.annotation.JsonProperty
import me.sun.notificationservice.domain.entity.member.MemberToken
import java.time.LocalDateTime

data class MemberTokenDto(
        @JsonProperty("access_token")
        val accessToken: String,
        @JsonProperty("expires_in")
        val accessTokenExpiresIn: Long,
        @JsonProperty("refresh_token")
        val refreshToken: String = "",
        @JsonProperty("refresh_token_expires_in")
        val refreshTokenExpiresIn: Long = 0
) {
    fun toMemberToken() = MemberToken(
            accessToken = accessToken,
            accessTokenExpirationDateTime = LocalDateTime.now().plusSeconds(accessTokenExpiresIn),
            refreshToken = refreshToken,
            refreshTokenExpirationDateTime = LocalDateTime.now().plusSeconds(refreshTokenExpiresIn)
    )

    fun updateOnlyAccessToken(memberToken: MemberToken) = MemberToken(
            accessToken = accessToken,
            accessTokenExpirationDateTime = LocalDateTime.now().plusSeconds(accessTokenExpiresIn),
            refreshToken = memberToken.refreshToken,
            refreshTokenExpirationDateTime = memberToken.refreshTokenExpirationDateTime
    )

    fun hasOnlyAccessToken() = refreshToken.isEmpty() || refreshTokenExpiresIn == 0L

    fun validateToken() {
        validateAccessToken()
        validateRefreshToken()
    }

    fun validateAccessToken() {
        if (accessToken.isEmpty() || accessTokenExpiresIn < 1) {
            throw IllegalArgumentException("Invalid acessToken. accessToken: $accessToken, accessTokenExpiresIn: $accessTokenExpiresIn")
        }
    }

    private fun validateRefreshToken() {
        if (refreshToken.isEmpty() || refreshTokenExpiresIn < 1) {
            throw IllegalArgumentException("Invalid refreshToken. refreshToken: $refreshToken, refreshTokenExpiresIn: $refreshTokenExpiresIn")
        }
    }
}
