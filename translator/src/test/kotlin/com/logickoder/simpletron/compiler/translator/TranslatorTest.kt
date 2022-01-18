package com.logickoder.simpletron.compiler.translator

import com.logickoder.simpletron.compiler.translator.utils.extractStatement
import com.logickoder.simpletron.translator.core.syntax.SyntaxError
import com.logickoder.simpletron.translator.statement.Statement
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.kotlin.UseConstructor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class TranslatorTest {
    private lateinit var translator: Translator

    @Before
    fun setup() {
        translator = mock(
            useConstructor = UseConstructor.parameterless(),
            defaultAnswer = Answers.CALLS_REAL_METHODS
        ) {
            on { lines } doReturn listOf(
                "50 rem this is a remark",
                "30 input x",
                "80 let u = 4 * (j - 56)",
                "10 print w",
                "35 if i == z goto 80",
                "70 goto 45",
                "99 end",
                "hello people",
                "23 keep it up"
            )
        }
    }

    @Test
    fun testThatTheDefaultStatementsAreComplete() {
        assertEquals(7, translator.statements.size)
    }

    @Test
    fun testExtractStatement() {
        for ((index, line) in translator.lines.withIndex()) {
            val syntax = translator.extractStatement(index)
            if (syntax is Statement) {
                val statement = translator.statements.find { it.statementName == line.split(Regex("\\s+"))[1] }
                assertEquals(statement?.statementName, syntax.keyword)
            }
        }
    }

    @Test
    fun testThatAnIncompleteStatementFails() {
        for ((index, statement) in translator.statements.withIndex()) {
            val name = statement.statementName
            if (name != "end") {
                val syntax = translator.extractStatement("${(index + 1) * 10} $name", index + 1)
                assert(syntax is SyntaxError)
                println((syntax as SyntaxError).message)
            }
        }
    }
}