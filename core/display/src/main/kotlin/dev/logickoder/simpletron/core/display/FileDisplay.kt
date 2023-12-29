package dev.logickoder.simpletron.core.display

import dev.logickoder.simpletron.core.common.status
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException

/**
 * @param path the path to the file to open
 * @throws IOException if an exception occurred while trying to open the
 * file
 */
internal class FileDisplay(path: String) : Display {
    override var isClosed = false

    override val newline: String = System.lineSeparator()

    private val writer: BufferedWriter = BufferedWriter(FileWriter(path))

    @Throws(IllegalStateException::class, IOException::class)
    override fun show(message: String?) {
        check(!isClosed) { status }
        message?.let { writer.write(it) }
        writer.flush()
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { status }
        writer.close()
        isClosed = true
    }
}
