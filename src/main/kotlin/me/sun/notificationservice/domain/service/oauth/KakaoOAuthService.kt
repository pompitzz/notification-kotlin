package me.sun.notificationservice.domain.service.oauth

import com.fasterxml.jackson.annotation.JsonProperty
import me.sun.notificationservice.common.TOKEN
import me.sun.notificationservice.common.URL
import me.sun.notificationservice.domain.entity.member.service.MemberTokenDto
import me.sun.notificationservice.domain.service.adapter.FormUrlencodedRequestInfo
import me.sun.notificationservice.domain.service.adapter.RestTemplateAdapter
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

@Service
class KakaoOAuthService {
    fun getOAuthProfile(accessToken: String): OAuthProfile {
        val formUrlencodedRequestInfo = FormUrlencodedRequestInfo(
                accessToken = accessToken,
                requestUrl = URL.KAKAO_PROFILE,
                requestMethod = HttpMethod.POST
        )
        val kakaoProfile = RestTemplateAdapter.requestWithFormUrlencoded<KakaoProfile>(formUrlencodedRequestInfo).body
                ?: throw IllegalArgumentException("Fail fetch kakaoProfile")

        return OAuthProfile(kakaoProfile.id, kakaoProfile.getNickName())
    }

    fun requestRefreshToken(refreshToken: String): MemberTokenDto {
        val formUrlencodedRequestInfo = FormUrlencodedRequestInfo(
                requestUrl = URL.KAKAO_REFRESH_TOKEN,
                requestMethod = HttpMethod.POST,
                requestBody = mapOf(
                        "grant_type" to "refresh_token",
                        "client_id" to TOKEN.KAKAO_REST_API,
                        "refresh_token" to refreshToken
                )
        )

        val responseEntity = RestTemplateAdapter.requestWithFormUrlencoded<MemberTokenDto>(formUrlencodedRequestInfo)

        return responseEntity.body ?: throw IllegalArgumentException("Fail refresh kakaoToken")
    }
}

data class OAuthProfile(val oauthId: Long, val nickName: String)

private data class KakaoProfile(
        val id: Long,
        @JsonProperty("kakao_account")
        val kakaoAccount: KakaoAccount?,
        val properties: Properties?
) {
    fun getNickName(): String {
        val nickname = kakaoAccount?.profile?.nickname ?: properties?.nickname
        if (nickname == null || nickname.isEmpty()) {
            throw IllegalArgumentException("nick must exist in either kakaAccount or properties")
        }
        return nickname
    }
}

private data class KakaoAccount(
        val profile: InnerProfile?
)

private data class InnerProfile(
        val nickname: String?
)

private data class Properties(
        val nickname: String?
)
