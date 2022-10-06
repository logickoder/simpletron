package com.logickoder.simpletron.core.utils

typealias Hex = String
typealias FloatJVM = java.lang.Float
typealias LongJVM = java.lang.Long

const val DIGIT_COUNT = 8

/**
 * Converts this float into hex
 * */
fun Float.toHex(): Hex {
    return "0x%0${DIGIT_COUNT}x".format(FloatJVM.floatToIntBits(this))
}

/**
 * Converts this hex to a floating point value
 */
fun Hex.hexToFloat(): Float {
    val hex = removePrefix("0x")
    return FloatJVM.intBitsToFloat(LongJVM.parseLong(hex, 16).toInt())
}