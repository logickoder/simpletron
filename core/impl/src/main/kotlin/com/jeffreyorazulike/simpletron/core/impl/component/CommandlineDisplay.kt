package com.jeffreyorazulike.simpletron.core.impl.component

import com.jeffreyorazulike.simpletron.core.component.Display
import com.jeffreyorazulike.simpletron.core.impl.utils.newline

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 6:36 PM
 *
 * @param stopValue the value used to stop entering input
 */
class CommandlineDisplay(stopValue: Int): Display() {

    override var isClosed: Boolean = false

    init {
        val welcomeMessage =
            "*** Welcome to Simpletron!                      ***${newline()}" +
                    "*** Please enter your program one instruction   ***${newline()}" +
                    "*** (or data word) at a time. I will display    ***${newline()}" +
                    "*** the location number and a question mark (?) ***${newline()}" +
                    "*** You then type the word for that location.   ***${newline()}" +
                    "*** Type $stopValue to stop entering your program.  ***${newline()}"
        show(welcomeMessage)
    }

    @Throws(IllegalStateException::class)
    override fun show(message: String?) {
        check(!isClosed) { closedMessage }
        print(message)
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { closedMessage }
        System.out.close()
        isClosed = true
    }
}