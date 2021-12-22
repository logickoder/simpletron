package com.jeffreyorazulike.simpletron.core.components

import kotlin.math.log10
import kotlin.math.pow

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:54 AM
 *
 * The base class defining the way
 * [com.jeffreyorazulike.simpletron.core.Simpletron] communicates with the
 * memory
 */
abstract class Memory {

    /**
     * @return the max word a memory location can contain
     */
    abstract val maxWord: Int

    /**
     * @return the min word a memory location can contain
     */
    abstract val minWord: Int

    /**
     * @return the size of this memory
     */
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
    abstract operator fun set(address: Int, value: Int)

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
    abstract operator fun get(address: Int): Int

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
    override val maxWord: Int = 9999
    override val minWord: Int = maxWord * -1
    override val size: Int = 1000

    /**
     * Represents the memory
     */
    private val memory = ShortArray(size)

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun set(address: Int, value: Int) {
        if (value in minWord..maxWord) {
            memory[address] = value.toShort()
        }
    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    override fun get(address: Int): Int {
        return memory[address].toInt()
    }
}

fun Memory.separator() = 10.0.pow(log10(maxWord.toDouble()).toInt() - 1).toInt()