package com.logickoder.simpletron.core.instructions

import com.logickoder.simpletron.core.component.Memory
import com.logickoder.simpletron.core.utils.toHex
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 29 at 4:54 PM
 *
 */
class InstructionsExtrasTest {
    private lateinit var memory: Memory

    @Before
    fun setupMemory() {
        memory = memory()
    }

    @Test
    fun invalidStringConvertsToZero() {
        assertEquals(0f, "j ju ".toInstruction(memory))
        assertEquals(0f, "-x".toInstruction(memory))
    }

    @Test
    fun singleIntegerStringConvertsToTheValidInstruction() {
        assertEquals(1099f, "01099".toInstruction(memory))
        assertEquals(1099f, "0199".toInstruction(memory))
        assertEquals(13099f, "13099".toInstruction(memory))
        assertEquals(13099f, "1399".toInstruction(memory))
        assertEquals(13149f, "131499".toInstruction(memory))
    }

    @Test
    fun twoIntegersSeparatedByCharactersConvertsToASingleInstruction() {
        assertEquals(1099f, "01 099".toInstruction(memory))
        assertEquals(1099f, "01 99".toInstruction(memory))
        assertEquals(13099f, "13 099".toInstruction(memory))
        assertEquals(13099f, "13 -99".toInstruction(memory))
        assertEquals(13001f, "13  1".toInstruction(memory))
        memory = memory(10_000)
        assertEquals(130499f, "13_499".toInstruction(memory))
    }

    @Test
    fun multipleIntegersSeparatedByCharactersConvertsToASingleInstructionWithTheRightLength() {
        assertEquals(13099f, "13 099 -023".toInstruction(memory))
        assertEquals(13099f, "13 -99 a".toInstruction(memory))
        assertEquals(13143f, "13  1 436".toInstruction(memory))
    }

    @Test
    fun hexStringConvertsToDigit() {
        var float = 12.888f
        assertEquals(float, float.toHex().toInstruction(memory))
        float = 999.2546f
        assertEquals(float, float.toHex().toInstruction(memory))
    }

    companion object {
        private fun memory(s: Int = 1_000) = mock<Memory> {
            on { size } doReturn s
        }
    }
}