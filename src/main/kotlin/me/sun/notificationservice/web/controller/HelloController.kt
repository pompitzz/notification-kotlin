package me.sun.notificationservice.web.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @RequestMapping("/hello")
    fun hello() = "Hello"
}
