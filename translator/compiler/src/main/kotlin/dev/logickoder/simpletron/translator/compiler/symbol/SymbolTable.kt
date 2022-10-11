package dev.logickoder.simpletron.translator.compiler.symbol

import dev.logickoder.simpletron.core.cpu.Instruction
import dev.logickoder.simpletron.translator.Subroutine
import dev.logickoder.simpletron.translator.symbol.Symbol

typealias Location = Int

/**
 * Holds the symbols used by the compiler during compilation
 */
internal interface SymbolTable {

    val constants: List<Float>

    /**
     * Holds the flags to be processed later by the compiler
     */
    val flags: List<Flag>

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
     * Flags a symbol as being unresolved
     *
     * If a symbol is to be flagged, it should be done before the instruction is added to the instructions list
     */
    fun flag(symbol: Symbol)

    /**
     * Convert an instruction and address to a complete instruction
     */
    fun format(instruction: Instruction, address: Location): Int

    companion object {
        const val DOES_NOT_EXIST = -1

        operator fun invoke(max: Location /* = kotlin.Int */): SymbolTable = SymbolTableImpl(max)
    }
}

