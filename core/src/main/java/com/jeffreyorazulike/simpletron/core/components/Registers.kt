package com.jeffreyorazulike.simpletron.core.components

/**
 * Holds the calculation the CPU is currently processing
 */
object Accumulator: Register(){
    override val name: String = this::class.simpleName.toString()
}

/**
 * Holds the operation the CPU is to perform
 */
object OperationCode: Register(){
    override val name: String = this::class.simpleName.toString()
}

/**
 * Holds the value the CPU is to use the [OperationCode] and work on
 */
object Operand: Register(){
    override val name: String = this::class.simpleName.toString()
}

/**
 * Holds the current memory location the CPU should get what to work on
 */
object InstructionCounter: Register(){
    override val name: String = this::class.simpleName.toString()
}

/**
 * Holds the value of the memory location gotten from the [InstructionCounter] to
 * prevent unnecessary reading from the memory
 */
object InstructionRegister: Register(){
    override val name: String = this::class.simpleName.toString()
}

fun defaultRegisters() = listOf(
    Accumulator, OperationCode, Operand, InstructionCounter, InstructionRegister
)


