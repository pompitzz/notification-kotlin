package me.sun.notificationservice.application.sender

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import me.sun.notificationservice.common.URL
import me.sun.notificationservice.application.adapter.FormUrlencodedRequestInfo
import me.sun.notificationservice.application.adapter.RestTemplateAdapter
import me.sun.notificationservice.application.model.kakao.KakaoMsg
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component

@Component
class KakaoMessageSender {
    private val objectMapper = ObjectMapper()

    init {
        objectMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    }

    fun send(template: KakaoMsg, accessToken: String) {
        val json = objectMapper.writeValueAsString(template)
        val formUrlencodedRequestInfo = FormUrlencodedRequestInfo(
                accessToken = accessToken,
                requestBody = mapOf("template_object" to json),
                requestUrl = URL.KAKAO_MESSAGE,
                requestMethod = HttpMethod.POST
        )
        RestTemplateAdapter.requestWithFormUrlencoded<String>(formUrlencodedRequestInfo)
    }
}
