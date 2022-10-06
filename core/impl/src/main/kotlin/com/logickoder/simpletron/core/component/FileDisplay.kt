package com.logickoder.simpletron.core.component

import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException

/**
 * @param path the path to the file to open
 * @throws IOException if an exception occurred while trying to open the
 * file
 */
class FileDisplay(path: String) : Display() {
    override var isClosed = false
    private val writer: BufferedWriter

    init {
        writer = BufferedWriter(FileWriter(path))
    }

    @Throws(IllegalStateException::class, IOException::class)
    override fun show(message: String?) {
        check(!isClosed) { closedMessage }
        message?.let { writer.write(it) }
        writer.flush()
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { closedMessage }
        writer.close()
        isClosed = true
    }
}
