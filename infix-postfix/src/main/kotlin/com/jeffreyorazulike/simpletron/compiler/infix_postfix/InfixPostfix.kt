package com.jeffreyorazulike.simpletron.compiler.infix_postfix

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

private val defaultCalculate: (Stack<Float>, Char) -> Float = { stack, operator ->
    val y = stack.pop()
    val x = stack.pop()

    when (operator) {
        PLUS -> x + y
        MINUS -> x - y
        MULTIPLICATION -> x * y
        DIVISION -> x / y
        REMAINDER -> x % y
        EXPONENTIATION -> x.toDouble().pow(y.toDouble()).toFloat()
        else -> 0f
    }
}

private val defaultTransform: (String) -> Float = { string ->
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
fun CharArray.getNumber(start: Int): Pair<Number, Int> {
    val number = StringBuilder(size)
    var i = start
    while (i < size) {
        val c = this[i]
        if (c.isDigit() || c == DECIMAL) {
            number.append(c)
            ++i
        } else {
            --i
            break
        }
    }

    return if (number.contains(DECIMAL))
        number.toString().toFloat() to i
    else
        number.toString().toInt() to i
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
 * Converts an infix expression to postfix
 * */
fun String.infixToPostfix(): String {
    val chars = plus(C_PARENTHESIS).replace(Regex("\\s+"), "").toCharArray().also {
        if (it.isEmpty()) return ""
    }
    val postfix = StringBuilder(chars.size)
    val stack = Stack<Char>().apply { push(O_PARENTHESIS) }

    var i = 0
    do {
        val char = chars[i]
        when {
            char.isDigit() || char == DECIMAL -> chars.getNumber(i).apply {
                postfix.append(this.first).append(SPACE)
                i = this.second
            }
            char.isLetter() -> postfix.append(char).append(SPACE)
            char == O_PARENTHESIS -> stack.push(char)
            char.isOperator() -> {
                while (stack.isNotEmpty() && stack.peek().precedence() >= char.precedence())
                    postfix.append(stack.pop()).append(SPACE)
                stack.push(char)
            }
            char == C_PARENTHESIS -> {
                while (stack.peek() != O_PARENTHESIS)
                    postfix.append(stack.pop()).append(SPACE)
                stack.pop()
            }
        }
    } while (++i < chars.size)
    return postfix.toString().trim()
}

/**
 * Evaluates a postfix expression
 *
 * @param transform changes a letter of digit to it corresponding int value
 * @param calculate evaluates a simple expression between two int values
 * */
fun String.postfixEvaluator(
    transform: (String) -> Float = defaultTransform,
    calculate: (Stack<Float>, Char) -> Float = defaultCalculate
): Number {
    val stack = Stack<Float>()
    val variables = plus(" $C_PARENTHESIS").split(Regex("\\s+"))

    for (variable in variables) {
        val char = variable[0]
        when {
            char == C_PARENTHESIS -> {
                val value = stack.pop()
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
            char.isOperator() -> stack.push(calculate(stack, char))
        }
    }
    return 0
}