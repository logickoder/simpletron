package dev.logickoder.simpletron.core.display

import dev.logickoder.simpletron.core.common.Closeable
import dev.logickoder.simpletron.core.common.Component

/**
 * Defines components that act as an output for all messages sent from simpletron
 */
interface Display : Component, Closeable {
    /**
     * Returns a platform dependent newline character
     * */
    val newline: String

    /**
     * @param message The message to display
     * @throws IllegalStateException if the display has been closed
     */
    @Throws(IllegalStateException::class)
    fun show(message: String?)

    companion object {
        operator fun invoke(type: DisplayType): Display {
            return when (type) {
                is DisplayType.CommandLine -> CommandlineDisplay(type.stopValue)
                is DisplayType.File -> FileDisplay(type.path)
            }
        }
    }
}