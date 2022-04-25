package com.android.myplacesandroid.domain.model

import com.android.myplacesandroid.domain.core.*
import com.google.android.gms.maps.model.LatLng

class Location(data: LatLng?) : ValueObject<LatLng?>() {
    override val value: ValueResult<LatLng?> = if (data != null) {
        ValueResult.valid(data)
    } else {
        ValueResult.inValid(error = ValueError.INVALID_LOCATION, failedValue = null)
    }
}

class Title(data: String) : ValueObject<String>() {
    override val value: ValueResult<String>
    val minLength = 5
    val maxLength = 50

    init {
        value = if (validateStringLengthInBoundaries(
                data,
                minLength = minLength,
                maxLength = maxLength
            )
        ) {
            ValueResult.valid(data)
        } else {
            ValueResult.inValid(error = ValueError.INVALID_TITLE, failedValue = data)
        }
    }
}

class Description(data: String) : ValueObject<String>() {
    override val value: ValueResult<String>
    val maxLength = 200

    init {
        value = if (validateMaxLength(data, maxLength = maxLength)) {
            ValueResult.valid(data)
        } else {
            ValueResult.inValid(error = ValueError.INVALID_DESCRIPTION, failedValue = data)
        }

    }

}

class Image(data: ByteArray) : ValueObject<ByteArray>() {
    override val value: ValueResult<ByteArray> = if (data.isNotEmpty()) {
        ValueResult.valid(data)
    } else {
        ValueResult.inValid(error = ValueError.INVALID_IMAGE, failedValue = data)
    }

}



