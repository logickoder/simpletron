package dev.logickoder.simpletron.core.contract

import dev.logickoder.simpletron.core.cpu.Halt
import dev.logickoder.simpletron.core.cpu.toInstruction
import dev.logickoder.simpletron.core.memory.stopValue
import kotlin.math.log10

/**
 *
 * Receives instructions input
 */
internal object InputInstructionsContract : Contract() {

    override fun execute(contractor: Contractor<*>) {
        with((contractor as? CPUContractor)?.executor ?: return) {
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
}

/**
 * Runs after program is loaded
 */
internal object ProgramLoadedContract : Contract() {

    override fun execute(contractor: Contractor<*>) {
        with((contractor as? CPUContractor)?.executor ?: return) {
            display.show(
                "${display.newline}*** Program loading completed ***" +
                        "${display.newline}*** Program execution begins  ***${display.newline}"
            )
        }
    }
}

/**
 *
 * Executes all instructions in memory
 */
internal object ExecuteInstructionsContract : Contract() {

    override fun execute(contractor: Contractor<*>) {
        with((contractor as? CPUContractor)?.executor ?: return) {
            // executes all the instructions until halt is reached
            while (true) {
                if (execute() is Halt) break
            }
        }
    }
}