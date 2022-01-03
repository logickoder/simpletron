package com.jeffreyorazulike.simpletron.core.component

import kotlin.math.log10
import kotlin.math.pow

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:54 AM
 *
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
     * @throws IllegalArgumentException if the value is less than the min_word
     * or greater than the max_word
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