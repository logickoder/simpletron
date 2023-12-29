package dev.logickoder.simpletron.core.cpu

import dev.logickoder.simpletron.core.common.Component
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.input.Input
import dev.logickoder.simpletron.core.memory.Memory

/**
 * The core part of simpletron that handles execution of instruction
 *
 * @property registers a list of registers that this [CPU] contains
 * @property instructions a list of the instructions that this [CPU] handle
 */
interface CPU : Component {
    val display: Display
    val input: Input
    val memory: Memory
    val registers: List<Register<*>>
    val instructions: List<Instruction>

    /**
     * Swaps the current component with the new one
     */
    fun <T : Component> swap(component: T)

    /**
     * Executes the next instruction
     *
     * @return the instruction that was executed if it exists
     */
    fun execute(): Instruction?

    companion object {
        operator fun invoke(memory: Memory, display: Display, input: Input): CPU = CPUImpl(memory, display, input)
    }
}