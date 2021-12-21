package com.jeffreyorazulike.simpletron.core.components

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 4:06 AM
 *
 * Defines the default registers in the CPU
 */
enum class Registers: Register {
    /**
     * Holds the calculation the CPU is currently processing
     */
    Accumulator,

    /**
     * Holds the operation the CPU is to perform
     */
    OperationCode,

    /**
     * Holds the value the CPU is to use the [OperationCode] and work on
     */
    Operand,

    /**
     * Holds the current memory location the CPU should get what to work on
     */
    InstructionCounter,

    /**
     * Holds the value of the memory location gotten from the [InstructionCounter] to
     * prevent unnecessary reading from the memory
     */
    InstructionRegister;

    override var value = 0
}
