package com.jeffreyorazulike.simpletron.core.impl.utils

import com.jeffreyorazulike.simpletron.core.component.*
import kotlin.math.absoluteValue
import kotlin.math.log10
import kotlin.math.pow
/**
 * returns a number in the power of tens that can be used to get the
 * [OperationCode] and [Operand]
 * */
fun Memory.separator() = 10.0.pow(log10(maxWord.toDouble()).toInt() - 1).toInt()

/**
 * returns the value that should be used to cancel [Input] when received as an [Instruction]
 * */
fun Memory.stopValue() =
    Math.negateExact(10.0.pow(log10(minWord.absoluteValue.toDouble()).toInt() + 2).toInt() - 1).toFloat()

/**
 * Displays the memory dump in hex
 * */
fun Memory.dump(display: Display) = with(display) {

    // the length of the hexadecimal representation of a floating-point value
    val valueLength = 8
    val rowHeaderSpace = log10(size.toDouble()).toInt()
    val horizontalStop = 10

    show("Memory:${newline()}%${rowHeaderSpace}s".format(" "))

    // show the table column headers
    val columnHeaderSpace = valueLength + 3
    for (i in 0 until horizontalStop) {
        display.show("%${columnHeaderSpace}d".format(i))
    }

    for (i in 0 until size) {
        if (i % horizontalStop == 0) show("${newline()}%${rowHeaderSpace}d".format(i))
        show(" ${this@dump[i].toHex()}")
    }
    show(newline())
}