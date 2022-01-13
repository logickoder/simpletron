package com.logickoder.simpletron.core.component

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:51 AM
 *
 * Defines components that act as an output for all messages sent from simpletron
 */
abstract class Display : com.logickoder.simpletron.core.component.Closeable {
    /**
     * @param message The message to display
     * @throws IllegalStateException if the display has been closed
     */
    @Throws(IllegalStateException::class)
    abstract fun show(message: String?)
}