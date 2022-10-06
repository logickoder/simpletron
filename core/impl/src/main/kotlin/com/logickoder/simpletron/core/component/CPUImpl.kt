package com.logickoder.simpletron.core.component

import com.logickoder.simpletron.core.instructions.Read
import com.logickoder.simpletron.core.registers.*
import com.logickoder.simpletron.core.utils.classInstances
import com.logickoder.simpletron.core.utils.classes

/**
 * Default implementation for [CPU]
 */
class CPUImpl(
    memory: Memory,
    display: Display,
    input: Input
) : CPU(memory, display, input) {

    private val instructionRegister by lazy { register<InstructionRegister>() }
    private val instructionCounter by lazy { register<InstructionCounter>() }
    private val operationCode by lazy { register<OperationCode>() }
    private val operand by lazy { register<Operand>() }

    override val registers: List<Register<*>> by lazy {
        classes<Register<*>>(setOf(Accumulator::class.java)).classInstances()
    }
    override val instructions: List<Instruction> by lazy {
        classes<Instruction>(setOf(Read::class.java)).classInstances()
    }

    override fun execute(): Instruction? = with(controlUnit) {
        // store the value from the current memory address to the instruction register
        instructionRegister.value = memory[instructionCounter.value]
        // update the instruction counter
        instructionCounter.value = instructionCounter.value + 1
        // store the operation code
        operationCode.value = (instructionRegister.value / memory.separator()).toInt()
        // store the operand
        operand.value = (instructionRegister.value % memory.separator()).toInt()
        // execute and return the instruction
        return instructions.find { op -> op.code == operationCode.value }?.apply {
            execute(this@CPUImpl)
        }
    }
}