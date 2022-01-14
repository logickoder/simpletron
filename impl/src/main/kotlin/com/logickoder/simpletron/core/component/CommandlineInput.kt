package com.logickoder.simpletron.core.component

import java.util.*

/**
 * An input that is gotten from the commandline
 */
class CommandlineInput : Input() {
    override var isClosed = false
    private val scanner = Scanner(System.`in`)

    override fun read(): String {
        check(!isClosed) { closedMessage }
        return scanner.nextLine()
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { closedMessage }
        isClosed = true
    }
}