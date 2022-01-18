package com.logickoder.simpletron.core.component

/**
 * Defines components that act as an output for all messages sent from simpletron
 */
abstract class Display : Closeable {
    /**
     * @param message The message to display
     * @throws IllegalStateException if the display has been closed
     */
    @Throws(IllegalStateException::class)
    abstract fun show(message: String?)
}

/**
 * Returns a platform dependent newline character
 * */
fun newline(): String = System.lineSeparator()