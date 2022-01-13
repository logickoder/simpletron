package com.logickoder.simpletron.compiler.translator

import com.logickoder.simpletron.compiler.translator.syntax.SyntaxError
import com.logickoder.simpletron.compiler.translator.utils.extractStatement
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
        val syntax = translator.extractStatement("20 if i == 2", 20)
        assert(syntax is SyntaxError)
    }

    @Test
    fun testLetFailsWithInvalidEquation() {
        val syntax = translator.extractStatement("80 let u = 4 ** (*j - 56)", 20)
        assert(syntax is SyntaxError)
        println((syntax as SyntaxError).message)
    }
}