package com.jeffreyorazulike.simpletron.core

import com.jeffreyorazulike.simpletron.core.components.Memory
import com.jeffreyorazulike.simpletron.core.utils.toInstruction
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
class InstructionUtilsTest {
    private lateinit var memory: Memory

    @Before
    fun setupMemory() {
        memory = memory()
    }

    @Test
    fun invalidStringConvertsToZero() {
        assertEquals(0, "j ju ".toInstruction(memory))
        assertEquals(0, "-x".toInstruction(memory))
    }

    @Test
    fun singleIntegerStringConvertsToTheValidInteger() {
        assertEquals(1099, "01099".toInstruction(memory))
        assertEquals(1099, "0199".toInstruction(memory))
        assertEquals(13099, "13099".toInstruction(memory))
        assertEquals(13099, "1399".toInstruction(memory))
        assertEquals(13149, "131499".toInstruction(memory))
    }

    @Test
    fun twoIntegerSeparatedByCharactersConvertsToASingleInteger() {
        assertEquals(1099, "01 099".toInstruction(memory))
        assertEquals(1099, "01 99".toInstruction(memory))
        assertEquals(13099, "13 099".toInstruction(memory))
        assertEquals(13099, "13 -99".toInstruction(memory))
        assertEquals(13001, "13  1".toInstruction(memory))
        memory = memory(10_000)
        assertEquals(130499, "13_499".toInstruction(memory))
    }

    @Test
    fun multipleIntegersSeparatedByCharactersConvertsToASingleIntegerWithTheRightLength() {
        assertEquals(13099, "13 099 -023".toInstruction(memory))
        assertEquals(13099, "13 -99 a".toInstruction(memory))
        assertEquals(13143, "13  1 436".toInstruction(memory))
    }

    companion object {
        private fun memory(s: Int = 1_000) = mock<Memory> {
            on { size } doReturn s
        }
    }
}