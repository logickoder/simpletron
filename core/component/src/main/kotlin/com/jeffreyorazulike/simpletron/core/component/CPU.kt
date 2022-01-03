package com.jeffreyorazulike.simpletron.core.component

import com.jeffreyorazulike.simpletron.core.component.CPU.ControlUnit

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

    abstract val registers: List<Register<*>>

    abstract val instructions: List<Instruction>

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
}