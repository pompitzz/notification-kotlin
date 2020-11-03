package me.sun.notificationservice.application.adapter

import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

object RestTemplateAdapter {
    private val restTemplate = RestTemplate()

    inline fun <reified T> requestWithFormUrlencoded(formUrlencodedRequestInfo: FormUrlencodedRequestInfo): ResponseEntity<T> {
        val (accessToken, requestBody, requestUrl, requestMethod) = formUrlencodedRequestInfo

        val headers = makeHeaders(accessToken)
        val body = LinkedMultiValueMap<String, String>(requestBody.mapValues { listOf(it.value) })
        val httpEntity = HttpEntity<MultiValueMap<String, String>>(body, headers)

        return exchange(requestUrl, requestMethod, httpEntity, T::class.java)
    }

    fun makeHeaders(accessToken: String?): HttpHeaders {
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString())

        if (accessToken != null) {
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        }
        return headers
    }

    fun <T> exchange(url: String, requestMethod: HttpMethod, httpEntity: HttpEntity<MultiValueMap<String, String>>, clazz: Class<T>) =
            restTemplate.exchange(url, requestMethod, httpEntity, clazz)
}

data class FormUrlencodedRequestInfo(
        val accessToken: String? = null,
        val requestBody: Map<String, String> = emptyMap(),
        val requestUrl: String,
        val requestMethod: HttpMethod
)

