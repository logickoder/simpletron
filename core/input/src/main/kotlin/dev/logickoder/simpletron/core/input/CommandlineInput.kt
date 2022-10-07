package dev.logickoder.simpletron.core.input

import java.util.*

/**
 * An input that is gotten from the commandline
 */
internal class CommandlineInput(private val source: Scanner = Scanner(System.`in`)) : Input {
    override var isClosed = false

    override fun hasNext() = if (isClosed) false else source.hasNext()

    override fun read(): String {
        check(!isClosed) { status }
        return source.nextLine()
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { status }
        isClosed = true
    }
}