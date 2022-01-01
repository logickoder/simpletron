package com.jeffreyorazulike.simpletron.core.utils

import com.jeffreyorazulike.simpletron.core.components.Instruction
import com.jeffreyorazulike.simpletron.core.components.Memory
import com.jeffreyorazulike.simpletron.core.components.Write
import java.util.regex.Pattern
import kotlin.math.log10

/**
 * Converts this float into hex
 * */
fun Float.toHex(): String {
    return "0x%08x".format(java.lang.Float.floatToIntBits(this))
}

fun String.hexToFloat(): Float {
    val hex = removePrefix("0x")
    return java.lang.Float.intBitsToFloat(java.lang.Long.parseLong(hex, 16).toInt())
}

/**
 *
 * Converts a valid string to an instruction
 *
 * A valid string is either a valid integer or hexadecimal
 * */
fun String.toInstruction(memory: Memory): Float {
    val operandLength = log10(memory.size.toDouble()).toInt()
    // determine if the string is a hex value or decimal
    return if (startsWith(prefix = "0x", ignoreCase = true)) {
        hexToFloat()
    } else {
        // remove all non digits from the string
        split(Pattern.compile("\\D+")).reduce { acc: String, s: String -> "$acc$s" }.run {
            when {
                // return 0 if the string is empty
                isEmpty() -> 0f
                // return the string with the excess removed if the length is the right length or greater
                length >= operandLength + 2 -> substring(0, operandLength + 2).toFloat()
                // if the length is lesser, add the correct amount of zeros between the opcode and operand
                else -> "${substring(0, 2)}%0${operandLength}d".format(substring(2).toInt()).toFloat()
            }
        }
    }
}

/**
 *
 * @param address the operand of the instruction
 *
 * Returns the correct instruction code, given the [Instruction] and address
 *
 * For example calling this function on [Write] with a [Memory] of 1,000 blocks
 * and an address 35 will give you 11035, on a 100 block [Memory] you get 1135
 * */
fun Instruction.code(memory: Memory, address: Int): Int {
    return code * memory.separator() + address
}