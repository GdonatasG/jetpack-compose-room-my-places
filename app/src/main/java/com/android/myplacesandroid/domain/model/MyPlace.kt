package com.android.myplacesandroid.domain.model

data class MyPlace(
    val lat: Double,
    val lng: Double,
    val title: Title,
    val description: Description,
    val image: ByteArray,
    val id: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MyPlace

        if (lat != other.lat) return false
        if (lng != other.lng) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (!image.contentEquals(other.image)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lat.hashCode()
        result = 31 * result + lng.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + (id ?: 0)
        return result
    }
}
