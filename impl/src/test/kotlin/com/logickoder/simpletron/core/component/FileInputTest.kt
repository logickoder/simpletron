package com.logickoder.simpletron.core.component

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

class FileInputTest {

    private lateinit var input: Input
    private lateinit var file: File

    @Before
    fun createFile() {
        file = File(FileInputTest::class.java.simpleName).apply{
            val text = StringBuilder()
            for (i in 1..10) text.append("$i $TEST_MESSAGE${newline()}")
            writeText(text.toString())
        }
    }

    @Before
    fun setUp() {
        input = FileInput(file.path)
    }

    @Test
    fun readAMessageFromFile(){
        assertEquals(Files.readAllLines(file.toPath()).first(), input.read())
    }

    @Test
    fun readMessagesFromFile() {
        Files.readAllLines(file.toPath()).forEach { line ->
            assertEquals(line, input.read())
        }
    }

    @Test
    fun hasNext() {
        var count = 1
        while (input.hasNext()) {
            assert(input.read().startsWith(count++.toString()))
        }
        assertEquals(10, count)
    }

    @Test
    fun throwsAnExceptionWhenReachingEOF() {
        for (i in 1..10) input.read()
        assertThrows(IllegalStateException::class.java) { input.read() }
    }

    @Test
    fun throwsAnExceptionWhenClosingInputAfterItHasBeenClosedAlready() {
        input.close()
        assertThrows(IllegalStateException::class.java) { input.close() }
    }

    @Test
    fun throwsAnExceptionWhenCallingReadWhenClosed(){
        input.close()
        assertThrows(IllegalStateException::class.java) { input.read() }
    }

    @After
    fun tearDown(){
        if(!input.isClosed) input.close()
        file.delete()
    }

    companion object {
        const val TEST_MESSAGE = "This is a file input test"
    }
}