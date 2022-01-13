package com.logickoder.simpletron.core.component

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:53 AM
 *
 * Defines components that act as input for all commands received from the user
 *
 * @property eofMessage A simple message showing that the component can't return an more input
 */
abstract class Input : com.logickoder.simpletron.core.component.Closeable {

    val eofMessage = "${this::class.simpleName} doesn't have any more input"

    /**
     *
     * @return the next line from the input source
     * @throws IllegalStateException if the input has been closed or
     * the end of the input has been reached
     */
    @Throws(IllegalStateException::class)
    abstract fun read(): String
}