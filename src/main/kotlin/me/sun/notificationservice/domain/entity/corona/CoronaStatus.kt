package me.sun.notificationservice.domain.entity.corona

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class CoronaStatus(
        @Id
        @Column(name = "corona_status_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @Enumerated(EnumType.STRING)
        val region: CoronaStatusRegion,
        val domesticOccurrenceCount: Int = 0,
        val foreignInflowCount: Int = 0,
        val measurementDateTime: LocalDateTime
)

enum class CoronaStatusRegion(
        val title: String
) {
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    INCHEON("인천"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    JEONBUL("전북"),
    JEONNAM("전남"),
    GYEONBUK("경북"),
    GYEONNAM("경남"),
    JEJU("제주"),
    OVERSEA("검역");

    companion object {
        private val store: HashMap<String, CoronaStatusRegion> =
                hashMapOf<String, CoronaStatusRegion>().apply { values().forEach { this[it.title] = it } }

        fun findByTitle(title: String): CoronaStatusRegion =
                store[title] ?: throw IllegalArgumentException("Invalid Title. title: $title")
    }
}
