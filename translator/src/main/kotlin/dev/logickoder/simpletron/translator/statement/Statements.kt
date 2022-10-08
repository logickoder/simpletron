package dev.logickoder.simpletron.translator.statement

import dev.logickoder.simpletron.translator.infix_postfix.evaluateExpression
import java.util.regex.Pattern

/**
 * Any text following the command rem is for documentation
 * purposes only and is ignored by the translator.
 *
 * Example: 50 rem this is a remark
 * */
abstract class Rem(override val lineNumber: Int, final override val action: String) : Statement

/**
 * Prompt the user to enter an integer.
 * Read that integer from the keyboard and store the integer in the specified variable.
 *
 * Example: 30 input x
 *
 * @property variables the variables to store the input received from the user
 * */
abstract class Input(override val lineNumber: Int, final override val action: String) : Statement {
    protected val variables: List<String>

    init {
        pattern.matcher(action).also { matcher -> require(matcher.matches()) }
        variables = action.split(Regex("\\s*,"))
    }

    companion object {
        private const val variable = "[a-z]+"
        private val pattern: Pattern = Pattern.compile("$variable(\\s*[,]\\s*$variable)*")
    }
}

/**
 * Assign the variable on the left the value on the right.
 * Note that an arbitrarily complex expression can appear to
 * the right of the equal sign.
 *
 * Example: 80 let u = 4 * (j - 56)
 *
 * @property variable the variable to store the final answer of the equation
 * @property action the right-hand side of the equation
 * */
abstract class Let(override val lineNumber: Int, final override val action: String) : Statement {
    protected val variable: String
    protected val expression: String

    init {
        require(action.count { it == '=' } == 1) {
            "there must be only one \"=\" in your equation"
        }
        action.split("=").toMutableList().apply {
            removeIf { it.isBlank() }
            require(size == 2) { "the equation was not written properly" }
            variable = this[0].trim()
            expression = this[1].trim()
            // validate the expression, this method with exit with an error if something is wrong with it
            expression.evaluateExpression()
        }
    }
}

/**
 * Display the value of the specified variable on screen
 *
 * Example: 10 print w
 *
 * @property symbols the alphanumeric values to print on the screen
 * */
abstract class Print(override val lineNumber: Int, final override val action: String) : Statement {
    protected val symbols: List<String>

    init {
        pattern.matcher(action).also { matcher -> require(matcher.matches()) }
        symbols = action.split(Regex("\\s*,"))
    }

    companion object {
        private const val variable = "([a-z]+|[0-9]+)"
        val pattern: Pattern = Pattern.compile("$variable(\\s*[,]\\s*$variable)*")
    }
}

/**
 * Compare two values for equality and transfer program control to
 * the specified line if the condition is true; otherwise,
 * continue execution with the next statement.
 *
 * Example: 35 if i == z goto 80
 *
 * @property symbols the alphanumeric values used in the expression
 * @property operator the logical operator used in the expression
 * @property gotoLocation the location to goto if the expression evaluates to true
 * */
abstract class If(override val lineNumber: Int, final override val action: String) : Statement {
    protected val symbols: List<String>
    protected val operator: String
    protected val gotoLocation: String

    init {
        val ifMatcher = ifPattern.matcher(action).also { matcher ->
            require(matcher.find(0))
            symbols = listOf(matcher.group(1), matcher.group(3))
            operator = matcher.group(2)
        }
        gotoPattern.matcher(action.substring(ifMatcher.end())).also { matcher ->
            require(matcher.find()) { "goto statement not found" }
            gotoLocation = matcher.group(1)
        }
    }

    companion object {
        private const val symbol = "([a-z]+|[-]?[0-9]+)"
        private val ifPattern = Pattern.compile("$symbol\\s*(==|<=|>=|<|>)\\s*$symbol")
        private val gotoPattern = Pattern.compile("\\s+goto\\s+(\\d+)")
    }
}

/**
 * Transfer program control to the specified line.
 *
 * Example: 70 goto 45
 *
 * @property line the number of the line to goto
 * */
abstract class Goto(override val lineNumber: Int, final override val action: String) : Statement {
    protected val line: Int

    init {
        require(action.toIntOrNull() != null) { "required line number, found \"$action\"" }
        line = action.toInt()
    }
}

/**
 * Calls a subroutine, which is similar to a function call in most programming languages
 *
 * Example: 20 gosub repeat
 *
 * @param action the name of the subroutine
 */
abstract class Gosub(override val lineNumber: Int, final override val action: String) : Statement {
    protected val name: String = pattern.matcher(action).let { matcher ->
        require(matcher.matches()) { "invalid name for a subroutine" }
        matcher.group(0)
    }

    companion object {
        private val pattern = Pattern.compile("[a-z]+")
    }
}

/**
 * Ends a subroutine and returns the program back to where the subroutine was called from
 *
 * Example: 80 return
 */
abstract class Return(override val lineNumber: Int, final override val action: String = "") : Statement

/**
 * Terminates program execution.
 *
 * Example: 99 end
 * */
abstract class End(override val lineNumber: Int, final override val action: String = "") : Statement