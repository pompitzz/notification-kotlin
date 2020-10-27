package me.sun.notificationservice.web.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
internal class HelloControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun helloTest() {
        mockMvc.get("/hello")
                .andDo { print() }
                .andExpect {
                    status { isOk }
                    content {
                        contentType(MediaType.TEXT_PLAIN)
                        string("Hello")
                    }
                }
    }
}
