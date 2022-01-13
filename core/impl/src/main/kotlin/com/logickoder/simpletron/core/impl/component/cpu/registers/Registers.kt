package com.logickoder.simpletron.core.impl.component.cpu.registers

/**
 * Holds the calculation the CPU is currently processing
 */
class Accumulator : com.logickoder.simpletron.core.component.Register<Float>() {
    override var value = 0f
}

/**
 * Holds the operation the CPU is to perform
 */
class OperationCode : com.logickoder.simpletron.core.component.Register<Int>() {
    override var value = 0
}

/**
 * Holds the value the CPU is to use the [OperationCode] and work on
 */
class Operand : com.logickoder.simpletron.core.component.Register<Int>() {
    override var value = 0
}

/**
 * Holds the current memory location the CPU should get what to work on
 */
class InstructionCounter : com.logickoder.simpletron.core.component.Register<Int>() {
    override var value = 0
}

/**
 * Holds the value of the memory location gotten from the [InstructionCounter] to
 * prevent unnecessary reading from the memory
 */
class InstructionRegister : com.logickoder.simpletron.core.component.Register<Float>() {
    override var value = 0f
}