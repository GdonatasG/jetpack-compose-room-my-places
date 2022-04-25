package com.android.myplacesandroid.domain.core

import java.lang.Exception

abstract class ValueObject<T> {
    abstract val value: ValueResult<T>

    fun isValid(): Boolean = value.error == null && value.data != null
    fun getOrCrash(): T {
        try {
            return value.data!!
        } catch (e: Exception) {
            val explanation =
                "Encountered a ValueObject failure at an unrecoverable point. Terminating."
            throw Error(explanation + " Failure was with value: ${value.failedValue.toString()}")
        }
    }

    fun getDataOrFailedValue(): T? {
        return value.data ?: value.failedValue
    }
}

enum class ValueError {
    INVALID_TITLE,
    INVALID_DESCRIPTION,
    INVALID_LOCATION,
    INVALID_IMAGE
}

data class ValueResult<T>(
    val data: T?,
    val error: ValueError?,
    val failedValue: T?
) {
    companion object {
        fun <T> valid(data: T): ValueResult<T> = ValueResult(
            data = data,
            error = null,
            failedValue = null
        )

        fun <T> inValid(error: ValueError, failedValue: T): ValueResult<T> = ValueResult(
            data = null,
            error = error,
            failedValue = failedValue
        )
    }
}
