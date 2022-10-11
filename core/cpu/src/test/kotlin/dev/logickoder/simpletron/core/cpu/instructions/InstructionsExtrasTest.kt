package dev.logickoder.simpletron.core.cpu.instructions

import dev.logickoder.simpletron.core.common.hex
import dev.logickoder.simpletron.core.cpu.toInstruction
import dev.logickoder.simpletron.core.memory.Memory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

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
    fun floatStringConvertsCorrectly() {
        assertEquals(1009.9f, "01 09.9".toInstruction(memory))
        assertEquals(1000.99f, "010.99".toInstruction(memory))
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
        assertEquals(float, float.hex.toInstruction(memory))
        float = 999.2546f
        assertEquals(float, float.hex.toInstruction(memory))
    }

    companion object {
        private fun memory(s: Int = 1_000) = mock<Memory> {
            on { size } doReturn s
        }
    }
}