package com.logickoder.simpletron.translator

import com.logickoder.simpletron.translator.syntax.SyntaxError
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.kotlin.UseConstructor
import org.mockito.kotlin.mock

class StatementsTest {
    private lateinit var translator: Translator

    @Before
    fun setup() {
        translator = mock(
            useConstructor = UseConstructor.parameterless(),
            defaultAnswer = Answers.CALLS_REAL_METHODS
        )
    }

    @Test
    fun testThatSyntaxErrorIsThrownOnAnIfWithoutAGoto() {
        assertThrows(SyntaxError::class.java) {
            translator.extractStatement("20 if i == 2", 20)
        }
    }

    @Test
    fun testLetFailsWithInvalidEquation() {
        assertThrows(SyntaxError::class.java) {
            translator.extractStatement("80 let u = 4 ** (*j - 56)", 20)
        }
    }
}