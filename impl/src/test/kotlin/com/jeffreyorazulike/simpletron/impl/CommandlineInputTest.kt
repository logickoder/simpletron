package com.jeffreyorazulike.simpletron.impl

import com.jeffreyorazulike.simpletron.core.components.Input
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 9:51 PM
 */
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