package dev.logickoder.simpletron.translator.statement

import dev.logickoder.simpletron.translator.infix_postfix.evaluateExpression

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