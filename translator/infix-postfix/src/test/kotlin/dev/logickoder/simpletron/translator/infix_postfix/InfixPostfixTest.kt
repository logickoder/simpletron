package dev.logickoder.simpletron.translator.infix_postfix

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class InfixPostfixTest {

    @Test
    fun isOperator() {
        assertEquals(true, EXPONENTIATION.isOperator())
        assertEquals(true, MULTIPLICATION.isOperator())
        assertEquals(false, O_PARENTHESIS.isOperator())
        assertEquals(false, C_PARENTHESIS.isOperator())
        assertEquals(false, SPACE.isOperator())
    }

    @Test
    fun getNumber() {
        fun check(number: Pair<Int, Int>, string: String, start: Int) {
            assertEquals(number, StringBuilder(string).getNumber(start))
        }
        check(890 to 2, "890 +", 0)
        check(90 to 4, "as890n+", 3)
        check(9025 to 4, "89025-+", 1)
    }

    @Test
    fun precedence() {
        assertEquals(1, PLUS.precedence())
        assertEquals(2, MULTIPLICATION.precedence())
        assertEquals(3, EXPONENTIATION.precedence())
        assertEquals(0, SPACE.precedence())
        assertEquals(0, 'a'.precedence())
    }

    @Test
    fun infixToPostfix() {
        fun check(initial: String, final: String) {
            assertEquals(final, initial.infixToPostfix())
        }

        check("(6 + 2) * 5 - 8 / 4", "6 2 + 5 * 8 4 / -")
        check("(6 + -2) * 5 - 8 / 4", "6 2 - 5 * 8 4 / -")
        check("(6 + 2) * 5 -- 8 / 4", "6 2 + 5 * 8 4 / +")
        check("(63 + 24) * 504 - 822 / 42", "63 24 + 504 * 822 42 / -")
        check("(6.3 + 24) * 5.04 - 82.2 / 42", "6.3 24 + 5.04 * 82.2 42 / -")
    }

    @Test
    fun postfixEvaluator() {
        fun check(equation: String, result: Number) {
            assertEquals(result, equation.postfixEvaluator())
        }

        check("6 2 + 5 * 8 4 / -", 38)
        check("63 24 + 504 * 822 42 / -", 43828.43f)
        check("6.3 24 + 5.04 * 82.2 42 / -", 150.75f)
    }

    @Test
    fun calculationFailsWithExtraIllegalOperators() {
        fun check(equation: String) {
            assertThrows(IllegalArgumentException::class.java) {
                equation.evaluateExpression()
            }
        }

        check("(6 + 2) ** 5 - 8 / 4")
        check("(6.3 + 24) * 5.04 ---% 82.2 / 42")
    }
}