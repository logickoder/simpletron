package dev.logickoder.simpletron.core.memory

import dev.logickoder.simpletron.core.common.Component
import kotlin.math.log10
import kotlin.math.pow

/**
 * A component that stores data gotten from the [Input] and results execution of [Instruction]s
 *
 * @property maxWord the largest word that can be stored in a memory location
 * @property minWord the smallest word that can be stored in a memory location
 * @property size the number of words this memory can hold at once
 */
interface Memory : Component {

    val size: Int

    val maxWord: Float
        get() = 10.0.pow(log10(size.toDouble()).toInt() + 2).toFloat() - 1

    val minWord: Float
        get() = maxWord * -1

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
    operator fun set(address: Number, value: Number)

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
    operator fun get(address: Number): Float

    companion object {
        operator fun invoke(size: Int = 1_000): Memory = MemoryImpl(size)
    }
}