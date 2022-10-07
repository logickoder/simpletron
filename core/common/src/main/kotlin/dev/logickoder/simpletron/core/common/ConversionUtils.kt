package dev.logickoder.simpletron.core.common

typealias Hex = String

const val DIGIT_COUNT = 8

/**
 * Converts this float into hex
 * */
val Float.hex: Hex
    get() = "0x%0${DIGIT_COUNT}x".format(toRawBits())

/**
 * Converts this hex to a floating point value
 */
val Hex.float: Float
    get() = Float.fromBits(removePrefix("0x").toInt(16))