package com.android.myplacesandroid.domain.core

fun validateStringLengthInBoundaries(
    input: String,
    minLength: Int,
    maxLength: Int
): Boolean = input.length in minLength..maxLength

fun validateMaxLength(
    input: String,
    maxLength: Int
): Boolean = input.length <= maxLength