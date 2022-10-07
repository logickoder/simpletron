package dev.logickoder.simpletron.core.memory

/**
 * A concrete implementation of the [Memory] having [size] blocks of memory
 */
internal class MemoryImpl(override val size: Int) : Memory {

    /**
     * Represents the memory
     */
    private val memory = FloatArray(size)

    @Throws(ArrayIndexOutOfBoundsException::class, IllegalArgumentException::class)
    override fun set(address: Number, value: Number) = with(value.toFloat()) {
        if (this != stopValue)
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