package com.logickoder.simpletron.core.component

/**
 * Defines components that act as input for all commands received from the user
 *
 * @property eofMessage A simple message showing that the component can't return an more input
 * @property source where the input is gotten from
 */
abstract class Input : Closeable {
    val eofMessage = "${this::class.simpleName} doesn't have any more input"
    abstract val source: Any

    /**
     * true if there is any more input to be produced
     */
    abstract fun hasNext(): Boolean

    /**
     *
     * @return the next line from the input source
     * @throws IllegalStateException if the input has been closed or
     * the end of the input has been reached
     */
    @Throws(IllegalStateException::class)
    abstract fun read(): String
}