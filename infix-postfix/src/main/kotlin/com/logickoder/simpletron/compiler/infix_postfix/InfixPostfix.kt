package com.logickoder.simpletron.compiler.infix_postfix

import java.util.*
import kotlin.math.pow

const val DECIMAL = '.'
const val SPACE = ' '
const val PLUS = '+'
const val MINUS = '-'
const val DIVISION = '/'
const val REMAINDER = '%'
const val EXPONENTIATION = '^'
const val MULTIPLICATION = '*'
const val O_PARENTHESIS = '('
const val C_PARENTHESIS = ')'

typealias Calculate = (Stack<Float>, Char) -> Unit
typealias Transform = (String) -> Float

private val defaultCalculate: Calculate = { values, operator ->
    val y = values.pop()
    val x = values.pop()
    when (operator) {
        PLUS -> x + y
        MINUS -> x - y
        MULTIPLICATION -> x * y
        DIVISION -> x / y
        REMAINDER -> x % y
        EXPONENTIATION -> x.toDouble().pow(y.toDouble()).toFloat()
        else -> 0f
    }.let { values.push(it) }
}

private val defaultTransform: Transform = { string ->
    when (string.length) {
        1 -> {
            val char = string.toCharArray()[0]
            if (char.isDigit()) string.toFloat() else char.code.toFloat()
        }
        else -> string.toFloat()
    }
}

/**
 * Returns true if this character is an operator, false otherwise
 * */
fun Char.isOperator(): Boolean {
    return when (this) {
        PLUS,
        MINUS,
        DIVISION,
        REMAINDER,
        EXPONENTIATION,
        MULTIPLICATION -> true

        else -> false
    }
}

/**
 *
 * Returns a whole number from the start index to the index of a non number character
 *
 * @param start the position to start looking for a number
 */
fun StringBuilder.getNumber(start: Int): Pair<Number, Int> {
    val number = StringBuilder(length)
    var i = start
    while (i < length) {
        val c = this[i]
        if (c.isDigit() || c == DECIMAL) {
            number.append(c)
            ++i
        } else {
            --i
            break
        }
    }

    return when (number.count { it == DECIMAL }) {
        0 -> number.toString().toInt()
        1 -> number.toString().toFloat()
        else -> {
            val index = number.indexOf(DECIMAL)
            val decimal = number.substring(index + 1).replace(DECIMAL.toString(), "")
            number.substring(0..index).plus(decimal).toFloat()
        }
    } to i
}

/**
 * Remove and return any operator directly after an operator in the equation
 * */
@Throws(IllegalArgumentException::class)
fun StringBuilder.correctOperator(position: Int): Char {

    fun mergeSigns(signs: String): String {
        return signs.count { it == MINUS }.let {
            when (it) {
                1 -> MINUS
                2 -> PLUS
                else -> SPACE
            }
        }.toString()
    }

    while (true) {
        val nextPos = position + 1
        val thisOp = this[position]
        val nextOp = this[nextPos]
        val errorMessage = "this operator '$nextOp' cannot be placed directly after '$thisOp'"
        // exit the method if either of the values in those positions are not operators
        if (!nextOp.isOperator() || !thisOp.isOperator()) return SPACE
        when (thisOp.precedence()) {
            1 -> {
                // if this operator is + or -, make sure the next is + or - too
                require(nextOp.precedence() == 1) { errorMessage }
                // remove the nextOp from the equation
                deleteCharAt(nextPos)
                // merge the 2 operators
                replace(position, nextPos, mergeSigns("$thisOp$nextOp"))
            }
            2, 3 -> {
                // if this operator is *, /, % or ^, make sure the next is + or -
                require(nextOp.precedence() == 1) { errorMessage }
                // check the next characters after this to see if they can be merged
                correctOperator(nextPos)
                return this[nextPos]
            }
        }
    }
}

/**
 * Returns thr precedence of a character if it is an operator or zero if it is not
 */
fun Char.precedence(): Int {
    return when (this) {
        PLUS, MINUS -> 1
        DIVISION, REMAINDER, MULTIPLICATION -> 2
        EXPONENTIATION -> 3
        else -> 0
    }
}

/**
 * Evaluates an expression and return it's answer
 * */
fun String.evaluateExpression(
    transform: Transform = defaultTransform,
    calculate: Calculate = defaultCalculate
) = infixToPostfix().postfixEvaluator(transform, calculate)

/**
 * Converts an infix expression to postfix
 * */
@Throws(IllegalArgumentException::class)
fun String.infixToPostfix(): String {
    // return an empty string if the equation is blank
    if (isBlank()) return ""
    // remove all whitespaces from the string and add the closing parenthesis
    val chars = StringBuilder(replace(Regex("\\s+"), "").plus(C_PARENTHESIS))
    val postfix = StringBuilder(chars.length)
    val characters = Stack<Char>().apply { push(O_PARENTHESIS) }
    val extraOps = Stack<Char>()

    var i = 0
    do {
        val char = chars[i]
        when {
            char.isDigit() || char == DECIMAL -> chars.getNumber(i).apply {
                val append = if (extraOps.isNotEmpty()) "0$SPACE$first$SPACE${extraOps.pop()}$SPACE" else "$first$SPACE"
                postfix.append(append)
                i = second
            }
            char.isLetter() -> postfix.append(char).append(SPACE)
            char == O_PARENTHESIS -> characters.push(char)
            char.isOperator() -> {
                chars.correctOperator(i).let { if (it != SPACE) extraOps += it }
                while (characters.isNotEmpty() && characters.peek().precedence() >= char.precedence())
                    postfix.append(characters.pop()).append(SPACE)
                characters.push(chars[i])
            }
            char == C_PARENTHESIS -> {
                while (characters.peek() != O_PARENTHESIS)
                    postfix.append(characters.pop()).append(SPACE)
                characters.pop()
            }
        }
    } while (++i < chars.length)
    return postfix.toString().trim()
}

/**
 * Evaluates a postfix expression
 *
 * @param transform changes a letter of digit to it corresponding int value
 * @param calculate evaluates a simple expression between two int values
 * */
fun String.postfixEvaluator(
    transform: Transform = defaultTransform,
    calculate: Calculate = defaultCalculate
): Number {
    val stack = Stack<Float>()
    val variables = plus(" $C_PARENTHESIS").split(Regex("\\s+"))

    for (variable in variables) {
        val char = variable[0]
        when {
            char == C_PARENTHESIS -> {
                val value = if (stack.isNotEmpty()) stack.pop() else 0f
                val valueInt = value.toInt()
                // if the value after the decimal point of the float is only zero
                return if (valueInt.toFloat() == value)
                // return the integer version
                    valueInt
                else
                // return the float version stopping at the 100th value
                    "%.2f".format(value).toFloat()
            }
            char.isLetterOrDigit() -> stack.push(transform(variable))
            char.isOperator() -> calculate(stack, char)
        }
    }
    return 0
}