package com.logickoder.simpletron.compiler.statements

import com.logickoder.simpletron.compiler.infix_postfix.*
import com.logickoder.simpletron.core.component.Instruction
import com.logickoder.simpletron.core.instructions.*
import com.logickoder.simpletron.translator.statement.statements.*
import com.logickoder.simpletron.translator.symbol.Symbol.LineNumber.Companion.toLineNumber

/**
 * Tells that a class can be compiled to machine code
 */
interface Compilable {
    /**
     * Compiles this statement to machine code which may span lines
     *
     * @param config the configuration used by this statement during translation
     */
    fun compile(config: StatementConfig): MachineCode
}

class RemImpl(lineNumber: Int, comment: String) : Rem(lineNumber, comment), Compilable {
    override fun compile(config: StatementConfig): MachineCode = mutableListOf()
}

class InputImpl(lineNumber: Int, variables: String) : Input(lineNumber, variables), Compilable {

    override fun compile(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        for (variable in variables) {
            add(config.format(instruction, variable))
        }
    }

    companion object {
        private val instruction = Read()
    }
}

class LetImpl(lineNumber: Int, equation: String) : Let(lineNumber, equation), Compilable {

    override fun compile(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        // transforms variables and constants to their respective memory locations
        val transform: Transform = { config.resolver(it).location.toFloat() }
        // find out if the initial value has been loaded
        var loaded = false
        val calculate: Calculate = { values, operator ->
            val y = values.pop().toInt()
            if (!loaded) {
                // load the initial value from memory
                add(config.format(Instructions.Load.instruction, values.pop().toInt()))
                loaded = true
            }
            // perform the operation on y
            add(config.format(instruction(operator), y))
        }
        // convert the expression to machine code
        expression.evaluateExpression(transform = transform, calculate = calculate)
        // store the final value into the memory
        add(config.format(Instructions.Store.instruction, variable))
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

class PrintImpl(lineNumber: Int, symbols: String) : Print(lineNumber, symbols), Compilable {

    override fun compile(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        for (symbol in symbols) {
            add(config.format(instruction, symbol))
        }
    }

    companion object {
        private val instruction = Write()
    }
}

class IfImpl(lineNumber: Int, expression: String) : If(lineNumber, expression), Compilable {

    override fun compile(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        val subtract: (Int, Int) -> Unit = { x, y ->
            // load x from the memory
            add(config.format(Instructions.Load.instruction, x))
            // subtract y from it
            add(config.format(Instructions.Minus.instruction, y))
        }

        val (x, y) = config.resolver(symbols[0]).location to config.resolver(symbols[1]).location
        when (operator) {
            "==", "<", "<=" -> subtract(x, y)
            ">", ">=" -> subtract(y, x)
        }
        val location = gotoLocation.toLineNumber()
        when (operator) {
            "==" -> {
                add(config.format(Instructions.BranchZero.instruction, location))
            }
            ">=", "<=" -> {
                add(config.format(Instructions.BranchZero.instruction, location))
                add(config.format(Instructions.BranchNeg.instruction, location))
            }
            ">", "<" -> {
                add(config.format(Instructions.BranchNeg.instruction, location))
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

class GotoImpl(lineNumber: Int, expression: String) : Goto(lineNumber, expression), Compilable {

    override fun compile(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        add(config.format(instruction, line.toLineNumber()))
    }

    companion object {
        private val instruction = Branch()
    }
}

class EndImpl(lineNumber: Int) : End(lineNumber), Compilable {
    override fun compile(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        add("${instruction.code}${config.format(0)}")
    }

    companion object {
        private val instruction = Halt()
    }
}

