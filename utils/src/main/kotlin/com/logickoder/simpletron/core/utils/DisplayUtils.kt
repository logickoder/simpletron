package com.logickoder.simpletron.core.utils

import com.logickoder.simpletron.core.component.CPU
import com.logickoder.simpletron.core.component.Display
import com.logickoder.simpletron.core.component.Memory
import com.logickoder.simpletron.core.component.newline
import kotlin.math.log10

/**
 * Shows the values of the registers on the display
 * */
fun CPU.dump() = with(controlUnit.display) {
    val registers = this@dump.registers
    val longestNameLength = registers.maxByOrNull { it.name.length }!!.name.length
    val integerValueLength = log10(controlUnit.memory.size.toDouble()).toInt()

    show("Registers:${newline()}")
    registers.forEach { register ->
        show("%-${longestNameLength}s ".format(register.name))
        val value = if (register.value is Float)
            (register.value as Float).toHex()
        else
            "%0${integerValueLength}d".format(register.value as Int)
        show(" %10s${newline()}".format(value))
    }
    show(newline())
}


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