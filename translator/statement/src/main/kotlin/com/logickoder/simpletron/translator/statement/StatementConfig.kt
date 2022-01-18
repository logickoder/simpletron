package com.logickoder.simpletron.translator.statement

import com.logickoder.simpletron.core.component.Instruction
import com.logickoder.simpletron.translator.core.config.Config
import com.logickoder.simpletron.translator.core.symbol.Symbol
import kotlin.math.log10

/**
 * Resolves a string encountered during translation to a symbol
 */
typealias SymbolResolver = (String) -> Symbol
/**
 * Lines of machine code generated during translation
 */
typealias MachineCode = List<String>

/**
 * Holds the values used by a [Statement] during translation
 */
open class StatementConfig(
    val resolver: SymbolResolver,
    val memorySize: Int
) : Config

fun StatementConfig.format(address: Int): String {
    return "%0${log10(memorySize.toDouble()).toInt()}d".format(address)
}

fun StatementConfig.format(symbol: String) = format(resolver(symbol).location)

fun StatementConfig.format(instruction: Instruction, address: Int): String {
    return "${instruction.code}${format(address)}"
}

fun StatementConfig.format(instruction: Instruction, symbol: String): String {
    return "${instruction.code}${format(symbol)}"
}