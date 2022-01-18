package com.logickoder.simpletron.translator.statement.statements

import com.logickoder.simpletron.compiler.infix_postfix.*
import com.logickoder.simpletron.core.component.Instruction
import com.logickoder.simpletron.core.instructions.*
import com.logickoder.simpletron.translator.core.symbol.Symbol.LineNumber.Companion.toLineNumber
import com.logickoder.simpletron.translator.statement.MachineCode
import com.logickoder.simpletron.translator.statement.Statement
import com.logickoder.simpletron.translator.statement.StatementConfig
import com.logickoder.simpletron.translator.statement.format
import java.util.regex.Pattern


/**
 * Any text following the command rem is for documentation
 * purposes only and is ignored by the translator.
 *
 * Example: 50 rem this is a remark
 * */
class Rem(comment: String) : Statement(comment) {
    init {
        require(comment.isNotBlank()) { "a comment must come after a $keyword statement" }
    }

    override fun translate(config: StatementConfig): MachineCode = mutableListOf<String>()
}

/**
 * Prompt the user to enter an integer.
 * Read that integer from the keyboard and store the integer in the specified variable.
 *
 * Example: 30 input x
 * */
class Input(symbols: String) : Statement(symbols) {
    init {
        pattern.matcher(action).also { matcher ->
            require(matcher.matches()) { "malformed $keyword statement." }
        }
    }

    override fun translate(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        val symbols = action.split(Regex("\\s*,"))
        for (symbol in symbols) {
            add(config.format(instruction, symbol))
        }
    }

    companion object {
        private const val variable = "[a-z]+"
        private val pattern: Pattern = Pattern.compile("$variable(\\s*[,]\\s*$variable)*")
        private val instruction = Read()
    }
}

/**
 * Assign the variable on the left the value on the right.
 * Note that an arbitrarily complex expression can appear to
 * the right of the equal sign.
 *
 * Example: 80 let u = 4 * (j - 56)
 * */
class Let(equation: String) : Statement(equation) {
    private val variable: String
    private val expression: String

    init {
        require(action.count { it == '=' } == 1) {
            "there must be only one \"=\" in your equation"
        }
        action.split("=").toMutableList().apply {
            removeIf { it.isBlank() }
            require(size == 2) { "the equation was not written properly" }
            variable = this[0]
            expression = this[1]
            // validate the expression, this method with exit with an error if something is wrong with it
            expression.evaluateExpression()
        }
    }

    override fun translate(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
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

/**
 * Display the value of the specified variable on screen
 *
 * Example: 10 print w
 * */
class Print(action: String) : Statement(action) {
    init {
        pattern.matcher(action).also { matcher ->
            require(matcher.matches()) { "malformed $keyword statement." }
        }
    }

    override fun translate(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        val symbols = action.split(Regex("\\s*,"))
        for (symbol in symbols) {
            add(config.format(instruction, symbol))
        }
    }

    companion object {
        private const val variable = "([a-z]+|[0-9]+)"
        val pattern: Pattern = Pattern.compile("$variable(\\s*[,]\\s*$variable)*")
        private val instruction = Write()
    }
}

/**
 * Compare two values for equality and transfer program control to
 * the specified line if the condition is true; otherwise,
 * continue execution with the next statement.
 *
 * Example: 35 if i == z goto 80
 * */
class If(expression: String) : Statement(expression) {
    private val symbols: List<String>
    private val operator: String
    private val gotoLocation: String

    init {
        val ifMatcher = ifPattern.matcher(action).also { matcher ->
            require(matcher.find(0)) { "invalid $keyword expression" }
            symbols = listOf(matcher.group(1), matcher.group(3))
            operator = matcher.group(2)
        }
        gotoPattern.matcher(action.substring(ifMatcher.end())).also { matcher ->
            require(matcher.find()) { "goto statement not found" }
            gotoLocation = matcher.group(1)
        }
    }

    override fun translate(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
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

    companion object {
        private const val symbol = "([a-z]+|[0-9]+)"
        private val ifPattern = Pattern.compile("$symbol\\s*(==|<=|>=|<|>)\\s*$symbol")
        private val gotoPattern = Pattern.compile("\\s+goto\\s+(\\d+)")
    }
}

/**
 * Transfer program control to the specified line.
 *
 * Example: 70 goto 45
 * */
class Goto(line: String) : Statement(line) {
    init {
        require(action.toIntOrNull() != null) { "required line number, found \"$line\"" }
    }

    override fun translate(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        add(config.format(instruction, action.toLineNumber()))
    }

    companion object {
        private val instruction = Branch()
    }
}

/**
 * Terminates program execution.
 *
 * Example: 99 end
 * */
class End : Statement("") {
    override fun translate(config: StatementConfig): MachineCode = mutableListOf<String>().apply {
        add("${instruction.code}${config.format(0)}")
    }

    companion object {
        private val instruction = Halt()
    }
}