package com.jeffreyorazulike.simpletron.core.components

import com.jeffreyorazulike.simpletron.core.components.CPU.ControlUnit
import com.jeffreyorazulike.simpletron.core.utils.classInstances
import com.jeffreyorazulike.simpletron.core.utils.separator

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

    init { resetDefaultRegisters() }

    open val registers: List<Register<*>> by lazy { classInstances(setOf<Class<*>>(CPU::class.java, this::class.java)) }

    open val instructions: List<Instruction> by lazy {
        classInstances(
            setOf<Class<*>>(
                CPU::class.java,
                this::class.java
            )
        )
    }

    private var _controlUnit = ControlUnit(memory, display, input)

    val controlUnit: ControlUnit get() = _controlUnit

    /**
     * Changes the components this CPU is connected to
     * */
    open fun changeComponent(display: Display? = null, input: Input? = null) {
        _controlUnit = ControlUnit(
            memory = _controlUnit.memory,
            display = display ?: _controlUnit.display,
            input = input ?: _controlUnit.input
        )
    }


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
        OperationCode.value = (InstructionRegister.value / memory.separator()).toInt()
        // store the operand
        Operand.value = (InstructionRegister.value % memory.separator()).toInt()
        // execute and return the instruction
        val instruction = instructions.find{ op -> op.code == OperationCode.value }?.apply {
            execute(controlUnit)
        }
        return instruction
    }
}