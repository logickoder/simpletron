package com.logickoder.simpletron.core.impl.component

import java.nio.file.Files
import java.nio.file.Path

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 8:13 PM
 *
 * @param path the path to the file to read from
 * @throws java.io.FileNotFoundException if the file is not found
 */
class FileInput(path: String) : com.logickoder.simpletron.core.component.Input() {
    override var isClosed = false
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