package com.logickoder.simpletron.core.impl.contract.contracts

import com.jeffreyorazulike.simpletron.core.contract.Contract
import com.jeffreyorazulike.simpletron.core.contract.Contractor
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.instructions.Halt
import com.jeffreyorazulike.simpletron.core.impl.contract.CPUContractor
import com.jeffreyorazulike.simpletron.core.impl.utils.newline
import com.jeffreyorazulike.simpletron.core.impl.utils.stopValue
import com.jeffreyorazulike.simpletron.core.impl.utils.toInstruction
import kotlin.math.log10

/**
 *
 * Receives instructions input
 */
class InputInstructionsContract : Contract() {

    override fun execute(contractor: Contractor<*>) {
        val cu = (contractor as? CPUContractor)?.executor?.controlUnit ?: return
        val stopValue = cu.memory.stopValue().toInt().toString()
        val padding = log10(cu.memory.size.toDouble()).toInt()
        var address = 0
        var userInput: String
        do {
            cu.display.show("%0${padding}d ? ".format(address))
            userInput = cu.input.read()
            cu.memory[address++] = userInput.toInstruction(cu.memory)
        } while (userInput != stopValue)
    }
}

/**
 * Runs after program is loaded
 */
class ProgramLoadedContract : Contract() {

    override fun execute(contractor: Contractor<*>) {
        val cu = (contractor as? CPUContractor)?.executor?.controlUnit ?: return
        cu.display.show(
            "${newline()}*** Program loading completed ***" +
                    "${newline()}*** Program execution begins  ***${newline()}"
        )
    }
}

/**
 *
 * Executes all instructions in memory
 */
class ExecuteInstructionsContract : Contract() {

    override fun execute(contractor: Contractor<*>) {
        val cpu = (contractor as? CPUContractor)?.executor ?: return
        // executes all the instructions until halt is reached
        while (true) {
            if (cpu.execute() is Halt) break
        }
    }
}