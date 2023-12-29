package dev.logickoder.simpletron.core.input

import dev.logickoder.simpletron.core.common.status
import java.util.*

/**
 * An input that is gotten from the commandline
 */
internal class CommandlineInput(override val source: Scanner = Scanner(System.`in`)) : Input {
    override var isClosed = false

    override fun hasNext() = if (isClosed) false else source.hasNext()

    @Throws(IllegalStateException::class)
    override fun read(): String {
        check(!isClosed) { status }
        return source.nextLine()
    }

    @Throws(IllegalStateException::class)
    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { status }
        isClosed = true
    }
}