package com.jeffreyorazulike.simpletron.core.components

/**
 * Holds the calculation the CPU is currently processing
 */
object Accumulator: Register()

/**
 * Holds the operation the CPU is to perform
 */
object OperationCode: Register()

/**
 * Holds the value the CPU is to use the [OperationCode] and work on
 */
object Operand: Register()

/**
 * Holds the current memory location the CPU should get what to work on
 */
object InstructionCounter: Register()

/**
 * Holds the value of the memory location gotten from the [InstructionCounter] to
 * prevent unnecessary reading from the memory
 */
object InstructionRegister: Register()

fun defaultRegisters() = listOf(
    Accumulator, OperationCode, Operand, InstructionCounter, InstructionRegister
)


