package dev.logickoder.simpletron.core.input

import dev.logickoder.simpletron.core.common.Closeable
import dev.logickoder.simpletron.core.common.Component

/**
 * Defines components that act as input for all commands received from the user
 *
 * @property eofMessage A simple message showing that the component can't return an more input
 */
interface Input : Component, Closeable {
    val eofMessage: String
        get() = "${this::class.simpleName} doesn't have any more input"

    /**
     * true if there is any more input to be produced
     */
    fun hasNext(): Boolean

    /**
     *
     * @return the next line from the input source
     * @throws IllegalStateException if the input has been closed or
     * the end of the input has been reached
     */
    @Throws(IllegalStateException::class)
    fun read(): String

    companion object {
        operator fun invoke(type: InputType): Input {
            return when (type) {
                is InputType.CommandLine -> CommandlineInput()
                is InputType.File -> FileInput(type.path)
            }
        }
    }
}