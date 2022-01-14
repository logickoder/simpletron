package com.logickoder.simpletron.core.component

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

class FileInputTest {

    private lateinit var input: com.logickoder.simpletron.core.component.Input
    private lateinit var file: File

    @Before
    fun createFile() {
        file = File(FileInputTest::class.java.simpleName).apply{
            val text = StringBuilder()
            val separator = newline()
            for (i in 1..10) text.append("$i $TEST_MESSAGE$separator")
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
    fun readMessagesFromFile(){
        Files.readAllLines(file.toPath()).forEach { line ->
            assertEquals(line, input.read())
        }
    }

    @Test
    fun throwsAnExceptionWhenReachingEOF(){
        for(i in 1..10) input.read()
        assertThrows(IllegalStateException::class.java) { input.read() }
    }

    @Test
    fun throwsAnExceptionWhenClosingInputAfterItHasBeenClosedAlready(){
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