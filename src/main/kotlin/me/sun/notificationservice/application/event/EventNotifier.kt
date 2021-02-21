package me.sun.notificationservice.application.event

interface EventNotifier {
    fun notifyEvent(): Boolean
}
