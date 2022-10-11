package dev.logickoder.simpletron.core.cpu

import dev.logickoder.simpletron.core.common.float
import dev.logickoder.simpletron.core.common.hex
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.memory.Memory
import dev.logickoder.simpletron.core.memory.overflow
import dev.logickoder.simpletron.core.memory.separator
import java.util.regex.Pattern
import kotlin.math.log10

/**
 * Finds and returns the required register from the cpu
 **/
inline fun <reified T : Register<*>> CPU.register(): T {
    return registers.find { it.name == T::class.simpleName } as T
}

fun CPU.error(message: String) {
    val ic = register<InstructionCounter>()
    // show the error message
    display.show(message)
    // update the next memory location with the Halt instruction
    memory[ic.value] = Halt.code(memory, ic.value)
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
): Boolean {
    return if (memory.overflow(value.toFloat())) {
        error("Memory Overflow${display.newline}")
        true
    } else {
        action(value)
        false
    }
}

private val nonDigitRegex = Pattern.compile("\\D+").toRegex()
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
        float
    } else {
        val decimal = kotlin.run {
            val index = lastIndexOf('.')
            if (index != -1) {
                substring(index..lastIndex)
            } else ""
        }

        removeSuffix(decimal)
            // remove all non digits from string
            .replace(nonDigitRegex, "")
            .run {
                when {
                    // return the string with the excess removed if the length is the right length or greater
                    length >= operandLength + 2 -> substring(0, operandLength + 2)
                    // if the length is lesser, add the correct amount of zeros between the opcode and operand
                    else -> try {
                        "${substring(0, 2)}%0${operandLength}d".format(substring(2).toInt())
                    } catch (e: Exception) {
                        ""
                    } + decimal
                }.toFloatOrNull() ?: 0f
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
    return code * memory.separator + address
}

/**
 * Shows the values of the registers on the display
 * */
fun CPU.dump() {
    val registers = this@dump.registers
    val longestNameLength = registers.maxByOrNull { it.name.length }!!.name.length
    val integerValueLength = log10(memory.size.toDouble()).toInt()

    display.show("Registers:${display.newline}")
    registers.forEach { register ->
        display.show("%-${longestNameLength}s ".format(register.name))
        val value = if (register.value is Float)
            (register.value as Float).hex
        else
            "%0${integerValueLength}d".format(register.value as Int)
        display.show(" %10s${display.newline}".format(value))
    }
    display.show(display.newline)
}


/**
 * Displays the memory dump in hex
 * */
fun Memory.dump(display: Display) = with(display) {

    // the length of the hexadecimal representation of a floating-point value
    val valueLength = 8
    val rowHeaderSpace = log10(size.toDouble()).toInt()
    val horizontalStop = 10

    show("Memory:${newline}%${rowHeaderSpace}s".format(" "))

    // show the table column headers
    val columnHeaderSpace = valueLength + 3
    for (i in 0 until horizontalStop) {
        display.show("%${columnHeaderSpace}d".format(i))
    }

    for (i in 0 until size) {
        if (i % horizontalStop == 0) show("${newline}%${rowHeaderSpace}d".format(i))
        show(" ${this@dump[i].hex}")
    }
    show(newline)
}