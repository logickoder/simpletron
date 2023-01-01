package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.core.cpu.*
import dev.logickoder.simpletron.translator.compiler.symbol.SymbolTable
import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.infix_postfix.*
import dev.logickoder.simpletron.translator.statement.Let
import dev.logickoder.simpletron.translator.symbol.Symbol
import dev.logickoder.simpletron.translator.symbol.toSymbol
import java.util.*

internal class LetImpl(lineNumber: Int, equation: String) : Let(lineNumber, equation) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            val constants = Stack<Symbol.Constant>()
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            // transforms variables and constants to their respective memory locations
            val transform: Transform = {
                val symbol = it.toSymbol(subroutine)
                val location = symbol.check(config, lineNumber)
                if (symbol is Symbol.Constant) constants.push(symbol)
                location.toFloat()
            }
            // find out if the initial value has been loaded
            var loaded = false
            val calculate: Calculate = { values, operator ->
                var yConst: Symbol.Constant? = null
                val y = values.pop().toInt().let {
                    if (it == SymbolTable.DOES_NOT_EXIST) {
                        yConst = constants.pop()
                        0
                    } else it
                }
                if (!loaded) {
                    // load the initial value from memory
                    val initial = values.pop().toInt().let {
                        if (it == SymbolTable.DOES_NOT_EXIST) {
                            table.flag(constants.pop())
                            0
                        } else it
                    }
                    add(table.format(Load, initial))
                    loaded = true
                }
                // perform the operation on y
                yConst?.let { table.flag(it) }
                add(table.format(instruction(operator), y))
            }
            // convert the expression to machine code
            val unhandledValue = expression.evaluateExpression(transform = transform, calculate = calculate)
            // normally only happens when the let statement includes only a single variable or constant, e.g
            // let u = 1000 or let u = s
            if (unhandledValue != 0) {
                val value = if (constants.isNotEmpty()) {
                    table.flag(constants.pop())
                    0
                } else unhandledValue.toInt()
                add(table.format(Load, value))
            }
            // store the final value into the memory
            add(table.format(Store, table.add(Symbol.Variable(variable, subroutine))))
        }
    }

    private fun instruction(operator: Char) = when (operator) {
        MINUS -> Subtract
        MULTIPLICATION -> Multiply
        DIVISION -> Divide
        REMAINDER -> Remainder
        EXPONENTIATION -> Exponent
        else -> Add
    }
}