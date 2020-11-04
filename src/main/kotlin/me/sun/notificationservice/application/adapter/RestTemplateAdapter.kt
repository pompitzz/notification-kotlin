package me.sun.notificationservice.application.adapter

import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

object RestTemplateAdapter {
    private val restTemplate = RestTemplate()

    inline fun <reified T> requestWithFormUrlencoded(formUrlencodedRequestInfo: FormUrlencodedRequestInfo): ResponseEntity<T> {
        val (accessToken, requestBody, requestUrl, requestMethod) = formUrlencodedRequestInfo

        val headers = makeHeaders(accessToken, MediaType.APPLICATION_FORM_URLENCODED)
        val body = LinkedMultiValueMap<String, String>(requestBody.mapValues { listOf(it.value) })
        val httpEntity = HttpEntity<MultiValueMap<String, String>>(body, headers)
        return exchange(requestUrl, requestMethod, httpEntity, T::class.java)
    }

    inline fun <reified T> requestWithJson(jsonRequestInfo: JsonRequestInfo): ResponseEntity<T> {
        val (accessToken, jsonBody, requestUrl, requestMethod) = jsonRequestInfo
        println(jsonBody)
        val headers = makeHeaders(accessToken, MediaType.APPLICATION_JSON)
        val httpEntity = HttpEntity(jsonBody, headers)
        return exchange(requestUrl, requestMethod, httpEntity, T::class.java)
    }

    fun makeHeaders(accessToken: String?, contentType: MediaType): HttpHeaders {
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, contentType.toString())

        if (accessToken != null) {
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        }
        return headers
    }

    fun <T> exchange(url: String, requestMethod: HttpMethod, httpEntity: HttpEntity<*>, clazz: Class<T>) =
            restTemplate.exchange(url, requestMethod, httpEntity, clazz)
}

data class FormUrlencodedRequestInfo(
        val accessToken: String? = null,
        val requestBody: Map<String, String> = emptyMap(),
        val requestUrl: String,
        val requestMethod: HttpMethod
)

data class JsonRequestInfo(
        val accessToken: String? = null,
        val json: String? = null,
        val requestUrl: String,
        val requestMethod: HttpMethod
)
