package com.logickoder.simpletron.compiler

import com.logickoder.simpletron.compiler.SymbolTable.Companion.DOES_NOT_EXIST
import com.logickoder.simpletron.core.component.Instruction
import com.logickoder.simpletron.translator.Subroutine
import com.logickoder.simpletron.translator.symbol.Symbol
import kotlin.math.log10

/**
 * Lines of machine code generated during translation
 */
typealias MachineCode = List<Int>

typealias Location = Int

/**
 * Holds the symbols used by the compiler during compilation
 */
interface SymbolTable {

    /**
     * Holds the flags to be processed later by the compiler
     */
    val flags: List<Pair<Symbol.LineNumber, Location>>

    /**
     * Returns the location of the symbol
     */
    operator fun get(symbol: Symbol): Location

    /**
     * Adds the symbol to the table and returns the location
     */
    fun add(symbol: Symbol): Location

    /**
     * Clear all symbols relating to a subroutine from the symbol table
     */
    fun clear(subroutine: Subroutine)

    /**
     * Flags a line number as being unresolved
     *
     * If a line number is to be flagged, it should be done before the instruction is added to the instructions list
     */
    fun flag(lineNumber: Symbol.LineNumber)

    /**
     * Convert an instruction and address to a complete instruction
     */
    fun format(instruction: Instruction, address: Location): Int

    companion object {
        const val DOES_NOT_EXIST = -1

        fun create(max: Location /* = kotlin.Int */): SymbolTable = SymbolTableImpl(max)
    }
}

private class SymbolTableImpl(max: Location /* = kotlin.Int */) : SymbolTable {
    private val symbols = mutableMapOf<Symbol, Location>()

    val symbolLocation: SymbolLocation = SymbolLocation(max)

    override val flags: MutableList<Pair<Symbol.LineNumber, Location>> = mutableListOf()

    override fun get(symbol: Symbol): Location {
        return symbols.getOrDefault(symbol, DOES_NOT_EXIST)
    }

    override fun add(symbol: Symbol): Location {
        return get(symbol).let { location ->
            if (location == DOES_NOT_EXIST) {
                when (symbol) {
                    is Symbol.LineNumber -> symbolLocation.counter
                    else -> --symbolLocation.storage
                }.also { symbols[symbol] = it }
            } else location
        }
    }

    override fun clear(subroutine: Subroutine) {
        symbols.keys.removeIf {
            when (it) {
                is Symbol.LineNumber -> it.subroutine == subroutine
                is Symbol.Variable -> it.subroutine == subroutine
                else -> false
            }
        }
    }

    override fun flag(lineNumber: Symbol.LineNumber) {
        flags.add(lineNumber to symbolLocation.counter)
    }

    private fun format(address: Location): String {
        symbolLocation.counter++
        return "%0${log10(symbolLocation.max.toDouble()).toInt()}d".format(address)
    }

    override fun format(instruction: Instruction, address: Location): Int {
        return "${instruction.code}${format(address)}".toInt()
    }
}

/**
 * Saves the current memory location
 */
private class SymbolLocation(val max: Location) {
    var counter: Location = 0
        set(value) {
            if (value < field)
                throw CompileError("the new instruction counter value must be greater than or equal to the previous")
            field = value
        }
    var storage: Location = max
        set(value) {
            if (value > field)
                throw CompileError("the new instruction counter value must be greater than or equal to the previous")
            field = value
        }
}