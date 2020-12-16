package me.sun.notificationservice.application.exception

class ParserFailException(message: String, cause: Throwable) : RuntimeException(message, cause)
class MessageSendException(message: String, cause: Throwable) : RuntimeException(message, cause)
