package dev.logickoder.simpletron.core.display

import dev.logickoder.simpletron.core.common.status

/**
 * @param stopValue the value used to stop entering input
 */
internal class CommandlineDisplay(stopValue: Int) : Display {

    override var isClosed: Boolean = false

    override val newline: String = System.lineSeparator()

    init {
        show(
            buildString {
                append("*** Welcome to Simpletron!                      ***$newline")
                append("*** Please enter your program one instruction   ***$newline")
                append("*** (or data word) at a time. I will display    ***$newline")
                append("*** the location number and a question mark (?) ***$newline")
                append("*** You then type the word for that location.   ***$newline")
                append("*** Type $stopValue to stop entering your program.  ***$newline")
            }
        )
    }

    @Throws(IllegalStateException::class)
    override fun show(message: String?) {
        check(!isClosed) { status }
        print(message)
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { status }
        System.out.close()
        isClosed = true
    }
}