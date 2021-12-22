package com.jeffreyorazulike.simpletron.core.components

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:51 AM
 *
 */
abstract class Display: AutoCloseable {
    /**
     * @param message The message to display
     * @throws IllegalStateException if the display has been closed
     */
    @Throws(IllegalStateException::class)
    abstract fun show(message: String?)

    companion object{
        const val CLOSED = "Display has been turned off"
    }
}