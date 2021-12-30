package com.jeffreyorazulike.simpletron.impl

import com.jeffreyorazulike.simpletron.core.components.Input
import java.util.*

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
        check(!isClosed) { closedMessage }
        isClosed = true
    }
}