package com.jeffreyorazulike.simpletron.impl

import com.jeffreyorazulike.simpletron.core.components.Input
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 23 at 2:22 PM
 */
class FileInputTest {

    private lateinit var input: Input
    private lateinit var file: File

    @Before
    fun createFile() {
        file = File(FileInputTest::class.java.simpleName).apply{
            val text = StringBuilder()
            for (i in 1..10) text.append("$i $TEST_MESSAGE\n")
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