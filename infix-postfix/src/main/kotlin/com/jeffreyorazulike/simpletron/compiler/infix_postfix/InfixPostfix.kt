package com.jeffreyorazulike.simpletron.compiler.infix_postfix

import java.util.*


const val SPACE = ' '
const val PLUS = '+'
const val MINUS = '-'
const val DIVISION = '/'
const val REMAINDER = '%'
const val EXPONENTIATION = '^'
const val MULTIPLICATION = '*'
const val O_PARENTHESIS = '('
const val C_PARENTHESIS = ')'

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
fun CharArray.getNumber(start: Int): Pair<Int, Int> {
    val number = StringBuilder(size)
    var i = start
    while (i < size) {
        val c = this[i]
        if (c.isDigit()) {
            number.append(c)
            ++i
        } else {
            --i
            break
        }
    }
    return number.toString().toInt() to i
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
    val chars = plus(C_PARENTHESIS).replace("\\s+", "").toCharArray().also {
        if (it.isEmpty()) return ""
    }
    val postfix = StringBuilder(chars.size)
    val stack = Stack<Char>().apply { push(O_PARENTHESIS) }

    var i = 0
    do {
        val char = chars[i]
        when {
            char.isDigit() -> chars.getNumber(i).apply {
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