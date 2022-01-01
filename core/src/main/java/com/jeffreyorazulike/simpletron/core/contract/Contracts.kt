package com.jeffreyorazulike.simpletron.core.contract

import com.jeffreyorazulike.simpletron.core.components.CPU
import com.jeffreyorazulike.simpletron.core.components.Halt
import com.jeffreyorazulike.simpletron.core.utils.newline
import com.jeffreyorazulike.simpletron.core.utils.stopValue
import com.jeffreyorazulike.simpletron.core.utils.toInstruction
import kotlin.math.log10

/**
 *
 * Gets invoked to receive instructions input
 */
class InputInstructionsContract : Contract() {

    override fun execute(controlUnit: CPU.ControlUnit) = with(controlUnit) {
        val stopValue = memory.stopValue().toInt().toString()
        val padding = log10(memory.size.toDouble()).toInt()
        var address = 0
        var userInput: String
        do {
            display.show("%0${padding}d ? ".format(address))
            userInput = input.read()
            memory[address++] = userInput.toInstruction(memory)
        } while (userInput != stopValue)
    }
}

/**
 * Gets invoked when program loading finishes
 */
class ProgramLoadedContract : Contract() {

    override fun execute(controlUnit: CPU.ControlUnit) {
        controlUnit.display.show(
            "${newline()}*** Program loading completed ***" +
                    "${newline()}*** Program execution begins  ***${newline()}"
        )
    }
}

/**
 *
 * Executes all instructions in the memory
 */
class ExecuteInstructionsContract(val cpu: CPU) : Contract() {

    override fun execute(controlUnit: CPU.ControlUnit) {
        // executes all the instructions until halt is reached
        while (true) {
            if (cpu.execute() is Halt) break
        }
    }
}