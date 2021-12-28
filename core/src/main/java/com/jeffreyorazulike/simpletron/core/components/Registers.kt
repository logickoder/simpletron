package com.jeffreyorazulike.simpletron.core.components

/**
 * Holds the calculation the CPU is currently processing
 */
object Accumulator: Register<Float>(){ override var value = 0f }

/**
 * Holds the operation the CPU is to perform
 */
object OperationCode: Register<Int>() { override var value = 0 }

/**
 * Holds the value the CPU is to use the [OperationCode] and work on
 */
object Operand: Register<Int>() { override var value = 0 }

/**
 * Holds the current memory location the CPU should get what to work on
 */
object InstructionCounter: Register<Int>() { override var value = 0 }

/**
 * Holds the value of the memory location gotten from the [InstructionCounter] to
 * prevent unnecessary reading from the memory
 */
object InstructionRegister: Register<Float>(){ override var value = 0f }

/**
 * Reset the value of the default registers back to 0
 * */
internal fun resetDefaultRegisters() {
    listOf(Accumulator, InstructionRegister).forEach { it.value = 0f }
    listOf(OperationCode, Operand, InstructionCounter).forEach { it.value = 0 }
}


