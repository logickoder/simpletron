package com.jeffreyorazulike.simpletron.core.components

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:53 AM
 *
 * The component for receiving input from users
 */
interface Input: AutoCloseable {
    /**
     *
     * @return the next line from the input source
     * @throws IllegalStateException if the input has been closed
     */
    @Throws(IllegalStateException::class)
    fun read(): String

    companion object {
        const val CLOSED = "Input has been turned off"
        const val CANCEL = -99999
    }
}