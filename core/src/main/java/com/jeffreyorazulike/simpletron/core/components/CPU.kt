package com.jeffreyorazulike.simpletron.core.components

import com.jeffreyorazulike.simpletron.core.components.CPU.ControlUnit
import org.reflections.Reflections

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:30 AM
 *
 * The core part of simpletron that handles execution of instruction
 *
 * @property registers a list of registers that this [CPU] contains
 * @property instructions a list of the instructions that this [CPU] handle
 * @property controlUnit the [ControlUnit] of this [CPU]
 */
abstract class CPU(memory: Memory, display: Display, input: Input) {

    init {
        // clear the value of the registers anytime an instance is created
        defaultRegisters().forEach { it.value = 0 }
    }

    open val registers: List<Register> by lazy {
        // retrieve all the registers defined in this package, which are the default registers
        val defaultRegisters = Reflections(CPU::class.java.packageName).getSubTypesOf(Register::class.java)
        // retrieve any operation defined in the package of any class that implements this interface
        val additionalRegisters = Reflections(this::class.java.packageName).getSubTypesOf(Register::class.java)
        // merge both of them and return the registers
        defaultRegisters.union(additionalRegisters).map {
            it.kotlin.objectInstance ?: it.getDeclaredConstructor().newInstance()
        }
    }

    open val instructions: List<Instruction> by lazy {
        // retrieve all the instructions defined in this package, which are the default instructions
        val defaultInstructions = Reflections(CPU::class.java.packageName).getSubTypesOf(Instruction::class.java)
        // retrieve any instruction defined in the package of any class that implements this interface
        val additionalInstructions = Reflections(this::class.java.packageName).getSubTypesOf(Instruction::class.java)
        // merge both of them and return the instructions
        defaultInstructions.union(additionalInstructions).map {
            it.getDeclaredConstructor().newInstance()
        }
    }

    open val controlUnit = ControlUnit(memory, display, input)

    /**
     * Executes the next instruction
     *
     * @return the instruction that was executed if it exists
     */
    abstract fun execute(): Instruction?

    /**
     * Gives [Instruction]s access to core components for them to execute correctly
     */
    class ControlUnit(val memory: Memory, val display: Display, val input: Input)

    companion object {
        /**
         *
         * @return the default implementation of the CPU
         */
        fun create(memory: Memory, display: Display, input: Input): CPU = CPUImpl(memory, display, input)
    }
}

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:45 AM
 *
 */
private class CPUImpl(memory: Memory, display: Display, input: Input) : CPU(memory, display, input) {

    override fun execute(): Instruction?  = with(controlUnit){
        // store the value from the current memory address to the instruction register
        InstructionRegister.value = memory[InstructionCounter.value]
        // update the instruction counter
        InstructionCounter.value = InstructionCounter.value + 1
        // store the operation code
        OperationCode.value = InstructionRegister.value / memory.separator()
        // store the operand
        Operand.value = InstructionRegister.value % memory.separator()
        // execute and return the instruction
        val instruction = instructions.find{ op -> op.code == OperationCode.value }?.apply {
            execute(controlUnit)
        }
        return instruction
    }
}