package dev.logickoder.simpletron.translator.symbol

import dev.logickoder.simpletron.translator.Subroutine
import dev.logickoder.simpletron.translator.infix_postfix.evaluateExpression
import dev.logickoder.simpletron.translator.syntax.SyntaxError

/**
 * Creates either a [Symbol.Constant] or [Symbol.Variable] from the given string
 */
fun String.toSymbol(subroutine: Subroutine): Symbol = when {
    Symbol.Variable.isInvalidName(this).not() -> Symbol.Variable(this, subroutine)
    else -> Symbol.Constant(toDataType())
}

/**
 * Creates a [DataType] from a string expression
 */
fun String.toDataType(): DataType {
    val isString: (String, Char) -> Boolean = { text, char ->
        text.length >= 2 && text.startsWith(char) && text.endsWith(char)
    }
    val isExpression: (String) -> Boolean = {
        kotlin.runCatching {
            it.evaluateExpression()
        }.isSuccess
    }
    val isArray: (String) -> Boolean = {
        it.startsWith("[") && it.endsWith("]")
    }
    return when {
        isString(this, '"') || isString(this, '\'') -> DataType.String(slice(1 until lastIndex))
        isArray(this) -> {
            // retrieve the array entry
            val array = slice(1 until lastIndex).trim()
            // get first inner array if it exists
            val innerArray = kotlin.run {
                val firstBracket = array.indexOfFirst { it == '[' }
                val lastBracket = array.indexOfLast { it == ']' }
                if (lastBracket < firstBracket || (lastBracket > firstBracket && firstBracket == -1))
                    throw SyntaxError("Malformed array: $array")
                else if (firstBracket != -1 && lastBracket != -1) {
                    array.substring(firstBracket..lastBracket).toDataType()
                } else null
            }
            // retrieve the remaining data types
            val entries = kotlin.run {
                // split and remove the blank entries from the start of the array
                val start = innerArray?.let {
                    array.substringBefore('[')
                } ?: array
                val startFiltered = start.split(",").map {
                    it.trim()
                }.filter {
                    it.isNotBlank()
                }
                // split and remove the blank entries from the end of the array
                val end = innerArray?.let {
                    array.substringAfterLast(']')
                } ?: ""
                val endFiltered = end.split(",").map {
                    it.trim()
                }.filter {
                    it.isNotBlank()
                }
                buildList {
                    addAll(startFiltered.map { it.toDataType() })
                    innerArray?.let {
                        add(it)
                    }
                    addAll(endFiltered.map { it.toDataType() })
                }
            }
            DataType.Array(entries)
        }

        isExpression(this) -> DataType.Number(evaluateExpression())
        else -> throw SyntaxError("$this is not a valid data type")
    }
}