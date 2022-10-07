package dev.logickoder.simpletron.core.display

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CommandlineDisplayTest {
    private lateinit var display: Display
    private val out = ByteArrayOutputStream()
    private val originalOut = System.out

    @Before
    fun setUp() {
        display = CommandlineDisplay(-99999)
    }

    @Before
    fun setUpStreams(){
        System.setOut(PrintStream(out))
    }

    @After
    fun restoreStreams(){
        System.setOut(originalOut)
    }

    @Test
    fun writesAMessageToTheCommandline() {
        display.show(TEST_MESSAGE)
        assertEquals(TEST_MESSAGE, out.toString())
        display.show("$TEST_MESSAGE${display.newline}$TEST_MESSAGE")
        assertEquals("$TEST_MESSAGE$TEST_MESSAGE${display.newline}$TEST_MESSAGE", out.toString())
    }

    @Test
    fun throwsAnExceptionWhenClosingDisplayAfterDisplayHasBeenClosedAlready(){
        display.close()
        assertThrows(IllegalStateException::class.java){ display.close() }
    }

    @Test
    fun throwsAnExceptionWhenCallingShowWhenClosed(){
        display.close()
        assertThrows(IllegalStateException::class.java){ display.show(TEST_MESSAGE) }
    }

    companion object {
        const val TEST_MESSAGE = "This is a commandline display test"
    }
}