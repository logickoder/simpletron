package com.logickoder.simpletron.core.component

import java.util.*

/**
 * An input that is gotten from the commandline
 */
class CommandlineInput : Input() {
    override var isClosed = false
    override val source: Scanner = Scanner(System.`in`)

    override fun hasNext() = if (isClosed) false else source.hasNext()

    override fun read(): String {
        check(!isClosed) { closedMessage }
        return source.nextLine()
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { closedMessage }
        isClosed = true
    }
}