package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.core.cpu.*
import dev.logickoder.simpletron.translator.compiler.symbol.Flag
import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.infix_postfix.*
import dev.logickoder.simpletron.translator.statement.Let
import dev.logickoder.simpletron.translator.symbol.Symbol
import dev.logickoder.simpletron.translator.symbol.toSymbol

internal class LetImpl(lineNumber: Int, equation: String) : Let(lineNumber, equation) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            val constants = mutableListOf<Flag>()
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            // transforms variables and constants to their respective memory locations
            val transform: Transform = {
                val symbol = it.toSymbol(subroutine)
                val location = symbol.check(config, lineNumber)
                if (symbol is Symbol.Constant) constants.add(Flag(symbol, location))
                location.toFloat()
            }
            // find out if the initial value has been loaded
            var loaded = false
            val calculate: Calculate = { values, operator ->
                val y = values.pop().toInt()
                if (!loaded) {
                    // load the initial value from memory
                    add(table.format(Load, values.pop().toInt()))
                    loaded = true
                }
                // perform the operation on y
                constants.firstOrNull { it.location == y }?.let { table.flag(it.symbol) }
                add(table.format(instruction(operator), y))
            }
            // convert the expression to machine code
            expression.evaluateExpression(transform = transform, calculate = calculate)
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