package com.jeffreyorazulike.simpletron.core.utils

import com.jeffreyorazulike.simpletron.core.components.*
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

fun Memory.dump(display: Display) {

}