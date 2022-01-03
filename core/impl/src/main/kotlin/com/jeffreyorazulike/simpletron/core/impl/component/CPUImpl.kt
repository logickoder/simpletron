package com.jeffreyorazulike.simpletron.core.impl.component

import com.jeffreyorazulike.simpletron.core.component.*
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.instructions.Read
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.registers.*
import com.jeffreyorazulike.simpletron.core.impl.utils.classInstances
import com.jeffreyorazulike.simpletron.core.impl.utils.register
import com.jeffreyorazulike.simpletron.core.impl.utils.separator

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 03 at 5:02 PM
 *
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

    override val registers: List<Register<*>> = classInstances(setOf(Accumulator::class.java))
    override val instructions: List<Instruction> = classInstances(setOf(Read::class.java))

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