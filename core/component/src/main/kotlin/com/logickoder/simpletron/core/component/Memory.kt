package com.logickoder.simpletron.core.component

import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.pow

/**
 * A component that stores data gotten from the [Input] and results execution of [Instruction]s
 *
 * @property maxWord the largest word that can be stored in a memory location
 * @property minWord the smallest word that can be stored in a memory location
 * @property size the number of words this memory can hold at once
 */
abstract class Memory(val size: Int) {

    val maxWord: Float = 10.0.pow(log10(size.toDouble()).toInt() + 2).toFloat() - 1

    val minWord: Float = maxWord * -1

    /**
     *
     * @param address the address to store the value
     * @param value the value to be stored in the memory position
     *
     * @throws IllegalArgumentException if the value is less than the [minWord]
     * or greater than the [maxWord]
     *
     * @throws ArrayIndexOutOfBoundsException if an address less than 0 or
     * greater than or equal to the size of the memory
     */
    @Throws(IllegalArgumentException::class, ArrayIndexOutOfBoundsException::class)
    abstract operator fun set(address: Number, value: Number)

    /**
     *
     * @param address the address of the value to be returned
     *
     * @return the value at the address
     *
     * @throws ArrayIndexOutOfBoundsException if an address less than 0 or
     * greater than or equal to the size of the memory
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    abstract operator fun get(address: Number): Float
}

/**
 * Returns a number in the power of tens that can be used to get the separate
 * the value at an address to its respective operation code and operand pairs
 * */
fun Memory.separator(): Int {
    return 10.0.pow(
        log10(maxWord.toDouble()).toInt() - 1
    ).toInt()
}

/**
 * Returns the value that should be used to cancel [Input] when received as an [Instruction]
 * */
fun Memory.stopValue(): Float {
    return Math.negateExact(
        10.0.pow(
            log10(minWord.absoluteValue.toDouble()).toInt() + 2
        ).toInt() - 1
    ).toFloat()
}

/**
 * Checks if this number will cause an overflow error if placed in the memory
 */
fun Memory.overflow(value: Number) = value.toFloat() !in minWord..maxWord