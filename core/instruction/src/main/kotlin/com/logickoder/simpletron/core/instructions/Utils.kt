package com.logickoder.simpletron.core.instructions

import com.logickoder.simpletron.core.component.*
import com.logickoder.simpletron.core.registers.InstructionCounter
import com.logickoder.simpletron.core.registers.register
import com.logickoder.simpletron.core.utils.hexToFloat
import java.util.regex.Pattern
import kotlin.math.log10

fun CPU.error(message: String) = with(controlUnit) {
    val ic = register<InstructionCounter>()
    // show the error message
    display.show(message)
    // update the next memory location with the Halt instruction
    memory[ic.value] = Halt().code(memory, ic.value)
}

/**
 * @param value the number to check for overflow
 * @param action the action to perform if no overflow occurred
 *
 * Checks to see if the given value overflowed the memory
 * */
inline fun CPU.overflow(
    value: Number,
    action: (Number) -> Unit = {}
): Boolean = with(controlUnit) {
    return if (memory.overflow(value.toFloat())) {
        error("Memory Overflow${newline()}")
        true
    } else {
        action(value)
        false
    }
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