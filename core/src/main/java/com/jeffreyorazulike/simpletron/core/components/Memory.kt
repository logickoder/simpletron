package com.jeffreyorazulike.simpletron.core.components

import com.jeffreyorazulike.simpletron.core.utils.stopValue
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
abstract class Memory {

    abstract val maxWord: Float

    abstract val minWord: Float

    abstract val size: Int

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

    companion object {
        /**
         *
         * @return the default implementation of the memory
         */
        fun create(): Memory = MemoryImpl()
    }
}

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:57 AM
 *
 * A concrete implementation of the [Memory] having 1000 blocks of memory
 */
private class MemoryImpl: Memory() {

    override val size: Int = 1000
    override val maxWord = 10.0.pow(log10(size.toDouble()).toInt() + 2).toFloat() - 1
    override val minWord = maxWord * -1

    /**
     * Represents the memory
     */
    private val memory = FloatArray(size)

    @Throws(ArrayIndexOutOfBoundsException::class, IllegalArgumentException::class)
    override fun set(address: Number, value: Number) = with(value.toFloat()) {
        if (this != stopValue())
            require(this in minWord..maxWord) {
                "$this is not in the range of $minWord to $maxWord"
            }
        memory[address.toInt()] = this
    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun get(address: Number): Float {
        return memory[address.toInt()]
    }
}