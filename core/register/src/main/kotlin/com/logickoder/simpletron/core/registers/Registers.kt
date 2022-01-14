package com.logickoder.simpletron.core.registers

import com.logickoder.simpletron.core.component.CPU
import com.logickoder.simpletron.core.component.Register

/**
 * Holds the calculation the CPU is currently processing
 */
class Accumulator : Register<Float>() {
    override var value = 0f
}

/**
 * Holds the operation the CPU is to perform
 */
class OperationCode : Register<Int>() {
    override var value = 0
}

/**
 * Holds the value the CPU is to use the [OperationCode] and work on
 */
class Operand : Register<Int>() {
    override var value = 0
}

/**
 * Holds the current memory location the CPU should get what to work on
 */
class InstructionCounter : Register<Int>() {
    override var value = 0
}

/**
 * Holds the value of the memory location gotten from the [InstructionCounter] to
 * prevent unnecessary reading from the memory
 */
class InstructionRegister : Register<Float>() {
    override var value = 0f
}

/**
 * Finds and returns the required register
 **/
inline fun <reified T : Register<*>> CPU.register(): T {
    return registers.find { it.name == T::class.simpleName } as T
}