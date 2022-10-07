package dev.logickoder.simpletron.core.cpu

import dev.logickoder.simpletron.core.common.Component
import dev.logickoder.simpletron.core.common.classInstances
import dev.logickoder.simpletron.core.common.classes
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.input.Input
import dev.logickoder.simpletron.core.memory.Memory
import dev.logickoder.simpletron.core.memory.separator

/**
 * Default implementation for [CPU]
 */
internal class CPUImpl(
    override var memory: Memory,
    override var display: Display,
    override var input: Input
) : CPU {

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

    override fun <T : Component> swap(component: T) {
        when (component) {
            is Memory -> memory = component
            is Display -> display = component
            is Input -> input = component
        }
    }

    override fun execute(): Instruction? {
        // store the value from the current memory address to the instruction register
        instructionRegister.value = memory[instructionCounter.value]
        // update the instruction counter
        instructionCounter.value = instructionCounter.value + 1
        // store the operation code
        operationCode.value = (instructionRegister.value / memory.separator).toInt()
        // store the operand
        operand.value = (instructionRegister.value % memory.separator).toInt()
        // execute and return the instruction
        return instructions.find { op -> op.code == operationCode.value }?.apply {
            execute(this@CPUImpl)
        }
    }
}