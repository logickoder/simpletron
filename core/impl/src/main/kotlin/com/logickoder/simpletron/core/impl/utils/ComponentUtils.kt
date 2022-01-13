package com.logickoder.simpletron.core.impl.utils

import com.jeffreyorazulike.simpletron.core.impl.component.cpu.instructions.Halt
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.registers.InstructionCounter
import org.reflections.Reflections
import kotlin.math.log10

/**
 * Returns the instances of the specified types in the package of the class set of classes,
 * make sure the classes being instantiated have a no-args constructor
 *
 * @param classes set of classes to search for the given type in their package
 * */
inline fun <reified T> classInstances(classes: Set<Class<*>>): List<T> {
    // retrieve all the required classes defined in the packages of the param [classes]
    return classes.asSequence().map { clazz ->
        Reflections(clazz.packageName).getSubTypesOf(T::class.java)
        // merge all into one set
    }.reduce { acc, otherClasses -> acc.union(otherClasses) }.map {
        // return instances of each of them
        it.kotlin.objectInstance ?: it.getDeclaredConstructor().newInstance()
    }
}

/**
 * @param value the number to check for overflow
 * @param action the action to perform if no overflow occurred
 *
 * Checks to see if the given value overflowed the memory
 * */
inline fun com.logickoder.simpletron.core.component.CPU.overflow(
    value: Number,
    action: (Number) -> Unit = {}
): Boolean = with(controlUnit) {
    return if (value.toFloat() !in memory.minWord..memory.maxWord) {
        error("Memory Overflow${newline()}")
        true
    } else {
        action(value)
        false
    }
}

fun com.logickoder.simpletron.core.component.CPU.error(message: String) = with(controlUnit) {
    val ic = register<InstructionCounter>()
    // show the error message
    display.show(message)
    // update the next memory location with the Halt instruction
    memory[ic.value] = Halt().code(memory, ic.value)
}

/**
 * Finds and returns the required register
 * */
inline fun <reified T : com.logickoder.simpletron.core.component.Register<*>> com.logickoder.simpletron.core.component.CPU.register(): T {
    return registers.find { it.name == T::class.simpleName } as T
}

/**
 * Returns a platform dependent newline character
 * */
fun newline(): String = System.lineSeparator()

/**
 * Shows the values of the registers on the display
 * */
fun com.logickoder.simpletron.core.component.CPU.dump() = with(controlUnit.display) {
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