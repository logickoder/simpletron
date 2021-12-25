package com.jeffreyorazulike.simpletron.impl

import com.jeffreyorazulike.simpletron.core.Simpletron
import com.jeffreyorazulike.simpletron.core.components.CPU
import com.jeffreyorazulike.simpletron.core.components.Halt
import com.jeffreyorazulike.simpletron.core.components.stopValue

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 8:15 PM
 *
 */
class Simpletron(cpu: CPU) : Simpletron(cpu) {
    override var isRunning: Boolean = false

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
        display.show("""
                *** Program loading completed ***
                *** Program execution begins  ***
                """.trimIndent())
        // keep executing executions till you reach a halt instruction
        while(cpu.execute() !is Halt){}
    }

    @Throws(IllegalStateException::class)
    override fun shutdown() = with(cpu.controlUnit) {
        check(isRunning) { "Simpletron is already shutdown" }
        isRunning = false
    }
}