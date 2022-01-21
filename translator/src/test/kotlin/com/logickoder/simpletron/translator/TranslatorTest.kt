package com.logickoder.simpletron.translator

import org.junit.Before
import org.mockito.Answers
import org.mockito.kotlin.UseConstructor
import org.mockito.kotlin.mock

class TranslatorTest {
    private lateinit var translator: Translator

    @Before
    fun setup() {
        translator = mock(
            useConstructor = UseConstructor.parameterless(),
            defaultAnswer = Answers.CALLS_REAL_METHODS
        )
    }

    companion object {
        private val lines = listOf(
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