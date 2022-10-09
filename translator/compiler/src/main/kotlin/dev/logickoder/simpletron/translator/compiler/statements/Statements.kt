package dev.logickoder.simpletron.translator.compiler.statements

import dev.logickoder.simpletron.core.cpu.*
import dev.logickoder.simpletron.translator.compiler.Location
import dev.logickoder.simpletron.translator.compiler.SymbolTable.Companion.DOES_NOT_EXIST
import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.infix_postfix.*
import dev.logickoder.simpletron.translator.statement.*
import dev.logickoder.simpletron.translator.symbol.Symbol
import dev.logickoder.simpletron.translator.symbol.toSymbol
import dev.logickoder.simpletron.translator.syntax.syntaxError

private fun Symbol.check(config: CompilerConfig, lineNumber: Location) = when (this) {
    is Symbol.Constant -> config.table.add(this)
    is Symbol.Variable -> config.table[this].also { location ->
        if (location == DOES_NOT_EXIST) {
            throw "This variable \"${this.name}\", has not been defined".syntaxError(lineNumber, true)
        }
    }

    else -> throw AssertionError("Only variables or constants are valid for this check")
}

internal class RemImpl(lineNumber: Int, comment: String) : Rem(lineNumber, comment) {
    override fun <T : Config> translate(config: T) = buildList<Int> {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
        }
    }
}

internal class InputImpl(lineNumber: Int, variables: String) : Input(lineNumber, variables) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            for (variable in variables) {
                add(table.format(instruction, table.add(Symbol.Variable(variable, subroutine))))
            }
        }
    }

    companion object {
        private val instruction = Read()
    }
}

internal class LetImpl(lineNumber: Int, equation: String) : Let(lineNumber, equation) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            // transforms variables and constants to their respective memory locations
            val transform: Transform = { it.toSymbol(subroutine).check(config, lineNumber).toFloat() }
            // find out if the initial value has been loaded
            var loaded = false
            val calculate: Calculate = { values, operator ->
                val y = values.pop().toInt()
                if (!loaded) {
                    // load the initial value from memory
                    add(table.format(Instructions.Load.instruction, values.pop().toInt()))
                    loaded = true
                }
                // perform the operation on y
                add(table.format(instruction(operator), y))
            }
            // convert the expression to machine code
            expression.evaluateExpression(transform = transform, calculate = calculate)
            // store the final value into the memory
            add(table.format(Instructions.Store.instruction, table.add(Symbol.Variable(variable, subroutine))))
        }
    }

    private fun instruction(operator: Char) = when (operator) {
        MINUS -> Instructions.Minus.instruction
        MULTIPLICATION -> Instructions.Times.instruction
        DIVISION -> Instructions.Divide.instruction
        REMAINDER -> Instructions.Mod.instruction
        EXPONENTIATION -> Instructions.Power.instruction
        else -> Instructions.Plus.instruction
    }

    private enum class Instructions(val instruction: Instruction) {
        Load(Load()),
        Plus(Add()),
        Minus(Subtract()),
        Times(Multiply()),
        Divide(Divide()),
        Mod(Remainder()),
        Power(Exponent()),
        Store(Store())
    }
}

internal class PrintImpl(lineNumber: Int, symbols: String) : Print(lineNumber, symbols) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            for (symbol in symbols) {
                add(table.format(instruction, symbol.toSymbol(subroutine).check(config, lineNumber)))
            }
        }
    }

    companion object {
        private val instruction = Write()
    }
}

internal class IfImpl(lineNumber: Int, expression: String) : If(lineNumber, expression) {


    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            val subtract: (Int, Int) -> Unit = { x, y ->
                // load x from the memory
                add(table.format(Instructions.Load.instruction, x))
                // subtract y from it
                add(table.format(Instructions.Minus.instruction, y))
            }

            val x = symbols[0].toSymbol(subroutine).check(config, lineNumber)
            val y = symbols[1].toSymbol(subroutine).check(config, lineNumber)
            when (operator) {
                "==", "<", "<=" -> subtract(x, y)
                ">", ">=" -> subtract(y, x)
            }
            val lineNumber = Symbol.LineNumber(gotoLocation.toInt(), subroutine)
            val flag: Boolean
            val location = table[lineNumber].let {
                flag = it == DOES_NOT_EXIST
                if (flag) {
                    table.flag(lineNumber)
                    0
                } else it
            }

            when (operator) {
                "==" -> {
                    add(table.format(Instructions.BranchZero.instruction, location))
                }
                ">=", "<=" -> {
                    add(table.format(Instructions.BranchZero.instruction, location))
                    if (flag) table.flag(lineNumber)
                    add(table.format(Instructions.BranchNeg.instruction, location))
                }
                ">", "<" -> {
                    add(table.format(Instructions.BranchNeg.instruction, location))
                }
            }
        }
    }

    private enum class Instructions(val instruction: Instruction) {
        Load(Load()),
        Minus(Subtract()),
        BranchZero(BranchZero()),
        BranchNeg(BranchNeg())
    }
}

internal class GotoImpl(lineNumber: Int, expression: String) : Goto(lineNumber, expression) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            val lineNum = Symbol.LineNumber(line, subroutine)
            val location = table[lineNum].let {
                if (it == DOES_NOT_EXIST) {
                    table.flag(lineNum)
                    0
                } else it
            }
            add(table.format(instruction, location))
        }
    }

    companion object {
        private val instruction = Branch()
    }

}

internal class GosubImpl(lineNumber: Int, subroutineName: String) : Gosub(lineNumber, subroutineName) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            // execute the statements in the subroutine to be called
            val subroutine = subroutines.find { it.name == name }!!
            val newConfig = config.copy(subroutine = subroutine)
            subroutine.statements.forEach {
                addAll((it).translate(newConfig))
            }
            // clear the symbols of this subroutine from the table
            table.clear(subroutine)
        }
    }
}

internal class ReturnImpl(lineNumber: Int) : Return(lineNumber) {

    override fun <T : Config> translate(config: T) = buildList<Int> {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
        }
    }
}

internal class EndImpl(lineNumber: Int) : End(lineNumber) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            table.add(Symbol.LineNumber(lineNumber, subroutine))
            add(table.format(instruction, 0))
        }
    }

    companion object {
        private val instruction = Halt()
    }
}