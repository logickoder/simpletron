package dev.logickoder.simpletron.core.contract

import dev.logickoder.simpletron.core.cpu.CPU
import dev.logickoder.simpletron.core.cpu.Halt
import dev.logickoder.simpletron.core.cpu.toInstruction
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.memory.stopValue
import kotlin.math.log10

/**
 *
 * Receives instructions input
 */
internal object InputInstructionsContract : CpuContract() {

    override fun execute(contractor: CPU) = contractor.run {
        val stopValue = memory.stopValue.toInt().toString()
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
 * Runs after program is loaded
 */
internal object ProgramLoadedContract : DisplayContract() {

    override fun execute(contractor: Display) = contractor.run {
        show("${newline}*** Program loading completed ***${newline}*** Program execution begins  ***${newline}")
    }
}

/**
 *
 * Executes all instructions in memory
 */
internal object ExecuteInstructionsContract : CpuContract() {

    override fun execute(contractor: CPU) {
        // executes all the instructions until halt is reached
        while (true) {
            if (contractor.execute() is Halt) break
        }
    }
}