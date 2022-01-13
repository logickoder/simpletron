package com.logickoder.simpletron.core.component

import com.logickoder.simpletron.core.component.CPU.ControlUnit

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
abstract class CPU(memory: com.logickoder.simpletron.core.component.Memory, display: com.logickoder.simpletron.core.component.Display, input: com.logickoder.simpletron.core.component.Input) {

    abstract val registers: List<com.logickoder.simpletron.core.component.Register<*>>

    abstract val instructions: List<com.logickoder.simpletron.core.component.Instruction>

    private var _controlUnit = ControlUnit(memory, display, input)

    val controlUnit: ControlUnit get() = _controlUnit

    /**
     * Changes the components this CPU is connected to
     * */
    open fun changeComponent(display: com.logickoder.simpletron.core.component.Display? = null, input: com.logickoder.simpletron.core.component.Input? = null) {
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
    abstract fun execute(): com.logickoder.simpletron.core.component.Instruction?

    /**
     * Gives [Instruction]s access to core components for them to execute correctly
     */
    class ControlUnit(val memory: com.logickoder.simpletron.core.component.Memory, val display: com.logickoder.simpletron.core.component.Display, val input: com.logickoder.simpletron.core.component.Input)
}