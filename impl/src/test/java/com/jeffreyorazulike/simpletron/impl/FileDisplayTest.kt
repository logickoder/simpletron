package com.jeffreyorazulike.simpletron.impl

import com.jeffreyorazulike.simpletron.core.components.Display
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 10:00 PM
 */
class FileDisplayTest {
    private lateinit var display: Display
    private lateinit var file: File

    @Before
    fun setUp() {
        file = File(FileDisplayTest::class.java.simpleName)
        display = FileDisplay(file.path)
    }

    @After
    fun tearDown(){
        display.close()
        file.delete()
    }

    @Test
    fun writesAMessageToFile(){
        display.show(TEST_MESSAGE)
        assertEquals(TEST_MESSAGE, Files.readAllLines(file.toPath())[0])
    }

    @Test
    fun writesMultipleMessagesToFile(){
        val range = 1..10
        for(i in range)
            display.show("$TEST_MESSAGE\n")
        val lines = Files.readAllLines(file.toPath())
        assertEquals(10, lines.size)
        for (line in lines)
            assertEquals(TEST_MESSAGE, line)
    }

    @Test
    fun throwsAnExceptionWhenClosingDisplayAfterItHasBeenClosedAlready(){
        display.close()
        assertThrows(IllegalStateException::class.java) { display.close() }
    }

    @Test
    fun throwsAnExceptionWhenCallingShowWhenClosed(){
        display.close()
        assertThrows(IllegalStateException::class.java) { display.show(TEST_MESSAGE) }
    }

    companion object {
        const val TEST_MESSAGE = "This is a file display test"
    }
}