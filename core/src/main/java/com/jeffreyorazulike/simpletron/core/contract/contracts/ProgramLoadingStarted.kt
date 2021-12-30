package com.jeffreyorazulike.simpletron.core.contract.contracts

import com.jeffreyorazulike.simpletron.core.components.CPU
import com.jeffreyorazulike.simpletron.core.components.stopValue
import com.jeffreyorazulike.simpletron.core.contract.Contract
import com.jeffreyorazulike.simpletron.core.utils.toInstruction
import kotlin.math.log10

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 30 at 6:30 AM
 *
 */
class ProgramLoadingStarted(
    override val contract: (CPU) -> Unit = {}
) : Contract {
    override fun execute(cpu: CPU) = with(cpu.controlUnit) {
        val stopValue = memory.stopValue().toString()
        val padding = log10(memory.size.toDouble())
        var address = 0
        var userInput: String
        do {
            display.show("%0${padding}d ? ".format(address))
            userInput = input.read()
            memory[address++] = userInput.toInstruction(memory)
        } while (userInput != stopValue)
        super.execute(cpu)
    }
}