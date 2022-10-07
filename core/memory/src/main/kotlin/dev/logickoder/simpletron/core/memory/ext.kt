package dev.logickoder.simpletron.core.memory

import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.pow


/**
 * Returns a number in the power of tens that can be used to get the separate
 * the value at an address to its respective operation code and operand pairs
 * */
val Memory.separator: Int
    get() = 10.0.pow(log10(maxWord.toDouble()).toInt() - 1).toInt()


/**
 * Returns the value that should be used to cancel [Input] when received as an [Instruction]
 * */
val Memory.stopValue: Float
    get() = Math.negateExact(10.0.pow(log10(minWord.absoluteValue.toDouble()).toInt() + 2).toInt() - 1).toFloat()

/**
 * Checks if this number will cause an overflow error if placed in the memory
 */
fun Memory.overflow(value: Number) = value.toFloat() !in minWord..maxWord