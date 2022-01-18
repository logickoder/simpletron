package com.logickoder.simpletron.core.component

import java.nio.file.Files
import java.nio.file.Path

/**
 * @param path the path to the file to read from
 * @throws java.io.FileNotFoundException if the file is not found
 */
class FileInput(path: String) : Input() {
    override var isClosed = false
    override val hasNext: Boolean
        get() = index < lines.lastIndex
    private val lines: List<String>
    private var index = 0

    init {
        lines = Files.readAllLines(Path.of(path))
    }

    @Throws(IllegalStateException::class)
    override fun read(): String {
        check(!isClosed) { closedMessage }
        check(index < lines.size) { eofMessage }
        return lines[index++]
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { closedMessage }
        isClosed = true
    }
}
