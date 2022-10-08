package dev.logickoder.simpletron.translator.symbol

import dev.logickoder.simpletron.translator.Subroutine

/**
 * Creates either a [Symbol.Constant] or [Symbol.Variable] from the given string
 */
fun String.toSymbol(subroutine: Subroutine) = when {
    toFloatOrNull() != null -> Symbol.Constant(toFloat())
    else -> Symbol.Variable(this, subroutine)
}