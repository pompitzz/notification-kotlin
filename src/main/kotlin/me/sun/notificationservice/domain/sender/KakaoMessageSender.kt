package me.sun.notificationservice.domain.sender

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import me.sun.notificationservice.domain.sender.model.KakaoMsg
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class KakaoMessageSender(
        private val restTemplate: RestTemplate
) {
    private val objectMapper = ObjectMapper()

    init {
        objectMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    }

    fun send(template: KakaoMsg, accessToken: String) {
        val json = objectMapper.writeValueAsString(template)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString())
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")

        val requestBody = LinkedMultiValueMap<String, String>()
        requestBody["template_object"] = json

        val httpEntity = HttpEntity<MultiValueMap<String, String>>(requestBody, headers)
        restTemplate.exchange("https://kapi.kakao.com/v2/api/talk/memo/default/send", HttpMethod.POST, httpEntity, String::class.java)
    }
}
