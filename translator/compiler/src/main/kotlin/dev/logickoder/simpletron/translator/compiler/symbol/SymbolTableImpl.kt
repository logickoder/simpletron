package dev.logickoder.simpletron.translator.compiler.symbol

import dev.logickoder.simpletron.core.cpu.Instruction
import dev.logickoder.simpletron.translator.Subroutine
import dev.logickoder.simpletron.translator.compiler.CompileError
import dev.logickoder.simpletron.translator.compiler.symbol.SymbolTable.Companion.DOES_NOT_EXIST
import dev.logickoder.simpletron.translator.symbol.DataType
import dev.logickoder.simpletron.translator.symbol.Symbol
import kotlin.math.log10

internal class SymbolTableImpl(max: Location /* = kotlin.Int */) : SymbolTable {
    private val symbols = mutableMapOf<Symbol, Location>()

    private val symbolLocation: SymbolLocation = SymbolLocation(max)

    override val constants: MutableList<Float> = mutableListOf()

    override val flags: MutableList<Flag> = mutableListOf()

    override fun get(symbol: Symbol): Location {
        return symbols.getOrDefault(symbol, DOES_NOT_EXIST)
    }

    override fun add(symbol: Symbol): Location {
        val location = get(symbol)
        return if (location == DOES_NOT_EXIST) {
            when (symbol) {
                is Symbol.LineNumber -> symbolLocation.counter
                is Symbol.Variable -> --symbolLocation.storage
                is Symbol.Constant -> symbol.value.store()
            }.also {
                symbols[symbol] = it
            }
        } else location
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

    override fun flag(symbol: Symbol) {
        flags += Flag(symbol, symbolLocation.counter)
    }

    private fun format(address: Location): String {
        symbolLocation.counter++
        return "%0${log10(symbolLocation.max.toDouble()).toInt()}d".format(address)
    }

    override fun format(instruction: Instruction, address: Location): Int {
        return "${instruction.code}${format(address)}".toInt()
    }

    private fun DataType.store(): Location {
        when (this) {
            is DataType.Array -> {
                value.forEach {
                    it.store()
                }
            }

            is DataType.Number -> constants.add(value)

            is DataType.String -> {
                value.forEach { constants.add(it.code.toFloat()) }
                constants.add(value.length.toFloat())
                symbolLocation.counter += value.length
            }
        }
        return symbolLocation.counter++
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
}