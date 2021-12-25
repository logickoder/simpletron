package com.jeffreyorazulike.simpletron.impl

import com.jeffreyorazulike.simpletron.core.components.Display
import javax.management.remote.JMXConnectionNotification.CLOSED


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
            "*** Welcome to Simpletron!                      ***\n" +
            "*** Please enter your program one instruction   ***\n" +
            "*** (or data word) at a time. I will display    ***\n" +
            "*** the location number and a question mark (?) ***\n" +
            "*** You then type the word for that location.   ***\n" +
            "*** Type $stopValue to stop entering your program.  ***\n"
        show(welcomeMessage)
    }

    @Throws(IllegalStateException::class)
    override fun show(message: String?) {
        check(!isClosed) { closedMessage }
        print(message)
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { CLOSED }
        System.out.close()
        isClosed = true
    }
}