package com.jeffreyorazulike.simpletron.impl

import com.jeffreyorazulike.simpletron.core.components.Input
import java.util.*
import javax.management.remote.JMXConnectionNotification

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 8:08 PM
 *
 */
class CommandlineInput: Input() {
    override var isClosed = false
    private val scanner = Scanner(System.`in`)

    override fun read(): String {
        check(!isClosed) { closedMessage }
        return scanner.nextLine()
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { JMXConnectionNotification.CLOSED }
        scanner.close()
        isClosed = true
    }
}