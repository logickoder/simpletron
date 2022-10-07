package dev.logickoder.simpletron.core.input

import java.nio.file.Files
import java.nio.file.Path

/**
 * @param path the path to the file to read from
 * @throws java.io.FileNotFoundException if the file is not found
 */
internal class FileInput(path: String) : Input {
    override var isClosed = false

    private val source: String = path
    private val lines: List<String> = Files.readAllLines(Path.of(source))
    private var index = 0

    override fun hasNext() = index < lines.size

    @Throws(IllegalStateException::class)
    override fun read(): String {
        check(!isClosed) { status }
        check(index < lines.size) { eofMessage }
        return lines[index++]
    }

    override fun close() {
        // throws illegal state exception if the component is already closed
        check(!isClosed) { status }
        isClosed = true
    }
}
