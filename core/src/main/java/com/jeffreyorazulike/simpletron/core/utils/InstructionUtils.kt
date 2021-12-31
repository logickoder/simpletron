package com.jeffreyorazulike.simpletron.core.utils

import com.jeffreyorazulike.simpletron.core.components.Instruction
import com.jeffreyorazulike.simpletron.core.components.Memory
import com.jeffreyorazulike.simpletron.core.components.Write
import java.util.regex.Pattern
import kotlin.math.log10

/**
 *
 * Converts a valid string to an instruction
 * */
fun String.toInstruction(memory: Memory): Int {
    val operandLength = log10(memory.size.toDouble()).toInt()
    // remove all non digits from the string
    return split(Pattern.compile("\\D+")).reduce { acc: String, s: String -> "$acc$s" }.run {
        when {
            // return 0 if the string is empty
            isEmpty() -> 0
            // return the string with the excess removed if the length is the right length or greater
            length >= operandLength + 2 -> substring(0, operandLength + 2).toInt()
            // if the length is lesser, add the correct amount of zeros between the opcode and operand
            else -> "${substring(0, 2)}%0${operandLength}d".format(substring(2).toInt()).toInt()
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