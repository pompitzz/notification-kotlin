package me.sun.notificationservice.domain.entity.corona_evnet

import me.sun.notificationservice.domain.entity.corona.CoronaStatusRegion
import javax.persistence.AttributeConverter

private const val SEPARATOR = ","

class RegionSetConverter : AttributeConverter<RegionSet, String> {
    override fun convertToDatabaseColumn(attribute: RegionSet?): String {
        if (attribute == null) {
            return ""
        }
        return attribute.selectRegions.joinToString(SEPARATOR)
    }

    override fun convertToEntityAttribute(dbData: String?): RegionSet {
        if (dbData == null || dbData.isEmpty()) {
            return RegionSet()
        }
        val regionSet = dbData.split(SEPARATOR).map { CoronaStatusRegion.valueOf(it) }.toSet()
        return RegionSet(regionSet)
    }
}
