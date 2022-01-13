package com.logickoder.simpletron.core.impl.component

import com.jeffreyorazulike.simpletron.core.impl.utils.stopValue
import com.logickoder.simpletron.core.component.Memory

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 03 at 5:03 PM
 *
 * A concrete implementation of the [Memory] having 1000 blocks of memory
 */
class MemoryImpl : com.logickoder.simpletron.core.component.Memory(1000) {

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