package com.logickoder.simpletron.compiler.statements

import com.logickoder.simpletron.compiler.MachineCode
import com.logickoder.simpletron.compiler.SymbolTable
import com.logickoder.simpletron.compiler.SymbolTable.Companion.DOES_NOT_EXIST
import com.logickoder.simpletron.compiler.infix_postfix.*
import com.logickoder.simpletron.core.component.Instruction
import com.logickoder.simpletron.core.instructions.*
import com.logickoder.simpletron.translator.statement.statements.*
import com.logickoder.simpletron.translator.symbol.Symbol
import com.logickoder.simpletron.translator.symbol.toSymbol
import com.logickoder.simpletron.translator.syntax.syntaxError

/**
 * Tells that a class can be compiled to machine code
 */
interface Compilable {
    /**
     * Compiles this statement to machine code which may span lines
     *
     * @param table the configuration used by this statement during translation
     */
    fun compile(table: SymbolTable, index: Int): MachineCode
}

fun Symbol.check(table: SymbolTable, index: Int) = when (this) {
    is Symbol.Constant -> table.add(this)
    is Symbol.Variable -> table[this].also { location ->
        if (location == DOES_NOT_EXIST) {
            throw "This variable \"${this.name}\", has not been defined".syntaxError(index)
        }
    }
    else -> throw AssertionError("Only variables or constants valid for this check")
}

class RemImpl(lineNumber: Int, comment: String) : Rem(lineNumber, comment), Compilable {
    override fun compile(table: SymbolTable, index: Int): MachineCode = mutableListOf()
}

class InputImpl(lineNumber: Int, variables: String) : Input(lineNumber, variables), Compilable {

    override fun compile(table: SymbolTable, index: Int): MachineCode = mutableListOf<Int>().apply {
        for (variable in variables) {
            add(table.format(instruction, table.add(Symbol.Variable(variable))))
        }
    }

    companion object {
        private val instruction = Read()
    }
}

class LetImpl(lineNumber: Int, equation: String) : Let(lineNumber, equation), Compilable {

    override fun compile(table: SymbolTable, index: Int): MachineCode = mutableListOf<Int>().apply {
        // transforms variables and constants to their respective memory locations
        val transform: Transform = { it.toSymbol().check(table, index).toFloat() }
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
        add(table.format(Instructions.Store.instruction, table.add(Symbol.Variable(variable))))
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

    override fun compile(table: SymbolTable, index: Int): MachineCode = mutableListOf<Int>().apply {
        for (symbol in symbols) {
            add(table.format(instruction, symbol.toSymbol().check(table, index)))
        }
    }

    companion object {
        private val instruction = Write()
    }
}

class IfImpl(lineNumber: Int, expression: String) : If(lineNumber, expression), Compilable {

    override fun compile(table: SymbolTable, index: Int): MachineCode = mutableListOf<Int>().apply {
        val subtract: (Int, Int) -> Unit = { x, y ->
            // load x from the memory
            add(table.format(Instructions.Load.instruction, x))
            // subtract y from it
            add(table.format(Instructions.Minus.instruction, y))
        }

        val x = symbols[0].toSymbol().check(table, index)
        val y = symbols[1].toSymbol().check(table, index)
        when (operator) {
            "==", "<", "<=" -> subtract(x, y)
            ">", ">=" -> subtract(y, x)
        }
        val lineNumber = Symbol.LineNumber(gotoLocation.toInt())
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

    private enum class Instructions(val instruction: Instruction) {
        Load(Load()),
        Minus(Subtract()),
        BranchZero(BranchZero()),
        BranchNeg(BranchNeg())
    }
}

class GotoImpl(lineNumber: Int, expression: String) : Goto(lineNumber, expression), Compilable {

    override fun compile(table: SymbolTable, index: Int): MachineCode = mutableListOf<Int>().apply {
        val lineNum = Symbol.LineNumber(line)
        val location = table[lineNum].let {
            if (it == DOES_NOT_EXIST) {
                table.flag(lineNum)
                0
            } else it
        }
        add(table.format(instruction, location))
    }

    companion object {
        private val instruction = Branch()
    }
}

class EndImpl(lineNumber: Int) : End(lineNumber), Compilable {
    override fun compile(table: SymbolTable, index: Int): MachineCode = mutableListOf<Int>().apply {
        add(table.format(instruction, 0))
    }
}

