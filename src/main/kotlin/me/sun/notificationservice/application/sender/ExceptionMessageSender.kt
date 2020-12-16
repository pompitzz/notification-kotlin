package me.sun.notificationservice.application.sender

interface ExceptionMessageSender {
    fun send(e: Exception, title: String)
    fun send(e: Exception)
}
