package com.jeffreyorazulike.simpletron.impl

import com.jeffreyorazulike.simpletron.core.Simpletron
import com.jeffreyorazulike.simpletron.core.components.CPU
import com.jeffreyorazulike.simpletron.core.contract.ExecuteInstructionsContract
import com.jeffreyorazulike.simpletron.core.contract.InputInstructionsContract
import com.jeffreyorazulike.simpletron.core.contract.ProgramLoadedContract

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 8:15 PM
 *
 */
class SimpletronImpl(cpu: CPU) : Simpletron(cpu) {
    override var isRunning: Boolean = false

    override val contracts = listOf(
        InputInstructionsContract(),
        ProgramLoadedContract(),
        ExecuteInstructionsContract(cpu)
    )

    @Throws(IllegalStateException::class)
    override fun run(): Unit = with(cpu.controlUnit) {
        check(!isRunning) { "Simpletron is already started" }
        isRunning = true
        contracts.distinctBy { it.name }.forEach { it.execute(this) }
    }

    @Throws(IllegalStateException::class)
    override fun shutdown() = with(cpu.controlUnit) {
        check(isRunning) { "Simpletron is already shutdown" }
        isRunning = false
    }
}