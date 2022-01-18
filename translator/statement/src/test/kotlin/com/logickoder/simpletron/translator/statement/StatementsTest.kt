package com.logickoder.simpletron.translator.statement

import com.logickoder.simpletron.translator.core.symbol.Symbol
import com.logickoder.simpletron.translator.statement.statements.Let
import org.junit.Before
import org.junit.Test

/**
 *
 */
class StatementsTest {
    private lateinit var config: StatementConfig
    private var count = 0
    private val memorySize = 1000

    @Before
    fun setup() {
        count = 0
        val resolver: SymbolResolver = {
            when {
                it.endsWith("L", ignoreCase = true) -> {
                    Symbol.LineNumber(it.removeSuffix("L").toInt(), count)
                }
                it.toIntOrNull() != null -> {
                    Symbol.Constant(it.toFloat(), memorySize - ++count)
                }
                else -> {
                    Symbol.Variable(it, memorySize - ++count)
                }
            }
        }
        config = StatementConfig(memorySize = 1000, resolver = resolver)
    }


    @Test
    fun testIfTranslate() {
        Let("u = 4 * (j - 56)").translate(config).also {
            println(it)
        }
    }
}