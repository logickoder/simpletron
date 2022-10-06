package com.logickoder.simpletron.core.contract.contracts

import com.logickoder.simpletron.core.component.newline
import com.logickoder.simpletron.core.component.stopValue
import com.logickoder.simpletron.core.contract.CPUContractor
import com.logickoder.simpletron.core.contract.Contract
import com.logickoder.simpletron.core.contract.Contractor
import com.logickoder.simpletron.core.instructions.Halt
import com.logickoder.simpletron.core.instructions.toInstruction
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