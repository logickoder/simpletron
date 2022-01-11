package com.logikoder.simpletron.compiler.translator

import com.logikoder.simpletron.compiler.translator.utils.extractKeyword
import com.logikoder.simpletron.compiler.translator.utils.findKeyword
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
    fun testThatTheDefaultKeywordsAreComplete() {
        assertEquals(7, translator.keywords.size)
    }

    @Test
    fun testExtractKeyword() {
        for ((index, line) in translator.lines.withIndex()) {
            val syntax = translator.extractKeyword(index)
            if (syntax.resolved) {
                val keyword = translator.findKeyword(line.split(Regex("\\s+"))[1])
                assertEquals(keyword, syntax.keyword?.javaClass)
            } else {
                println(syntax.error?.message)
            }
        }
    }
}