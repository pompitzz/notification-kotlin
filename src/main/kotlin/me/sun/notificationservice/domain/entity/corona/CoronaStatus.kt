package me.sun.notificationservice.domain.entity.corona

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class CoronaStatus(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        val region: String,
        val domesticOccurrenceCount: Int,
        val foreignInflowCount: Int,
        val measuringTime: LocalDateTime
) {
        override fun toString(): String {
                return "CoronaStatus(id=$id, region='$region', domesticOccurrenceCount=$domesticOccurrenceCount, foreignInflowCount=$foreignInflowCount, measuringTime=$measuringTime)"
        }
}
