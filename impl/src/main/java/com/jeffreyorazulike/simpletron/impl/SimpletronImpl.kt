package com.jeffreyorazulike.simpletron.impl

import com.jeffreyorazulike.simpletron.core.Simpletron
import com.jeffreyorazulike.simpletron.core.components.CPU
import com.jeffreyorazulike.simpletron.core.components.Halt
import com.jeffreyorazulike.simpletron.core.components.stopValue
import com.jeffreyorazulike.simpletron.core.contracts.Contract
import com.jeffreyorazulike.simpletron.core.contracts.ProgramLoadingCompleted

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 8:15 PM
 *
 */
class SimpletronImpl(cpu: CPU) : Simpletron(cpu) {
    override var isRunning: Boolean = false
    override val contracts = mutableSetOf<Contract>()

    @Throws(IllegalStateException::class)
    override fun run(): Unit = with(cpu.controlUnit) {
        check(!isRunning) { "Simpletron is already started" }
        isRunning = true
        val stopValue = memory.stopValue().toString()
        var address = 0
        var userInput: String
        do {
            display.show(String.format("%03d ? ", address))
            userInput = input.read()
            memory[address++] = userInput.toInt()
        } while (userInput != stopValue)
        // execute the contract after program loading completed
        contracts.filterIsInstance<ProgramLoadingCompleted>().firstOrNull()?.execute(cpu)
        // keep executing executions till you reach a halt instruction
        while(cpu.execute() !is Halt){}
    }

    @Throws(IllegalStateException::class)
    override fun shutdown() = with(cpu.controlUnit) {
        check(isRunning) { "Simpletron is already shutdown" }
        isRunning = false
    }
}