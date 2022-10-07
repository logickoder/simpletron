package dev.logickoder.simpletron.core.input

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CommandlineInputTest {
    private lateinit var input: Input

    @Before
    fun setUp() {
        input = CommandlineInput()
    }

    @Test
    fun throwsAnExceptionWhenClosingInputAfterInputHasBeenClosedAlready(){
        input.close()
        Assert.assertThrows(IllegalStateException::class.java) { input.close() }
    }

    @Test
    fun throwsAnExceptionWhenCallingReadWhenClosed(){
        input.close()
        Assert.assertThrows(IllegalStateException::class.java) { input.read() }
    }
}