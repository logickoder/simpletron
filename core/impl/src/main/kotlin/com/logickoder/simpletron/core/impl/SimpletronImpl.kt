package com.logickoder.simpletron.core.impl

import com.jeffreyorazulike.simpletron.core.impl.contract.CPUContractor
import com.jeffreyorazulike.simpletron.core.impl.contract.contracts.ExecuteInstructionsContract
import com.jeffreyorazulike.simpletron.core.impl.contract.contracts.InputInstructionsContract
import com.jeffreyorazulike.simpletron.core.impl.contract.contracts.ProgramLoadedContract

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 8:15 PM
 *
 */
class SimpletronImpl(cpu: com.logickoder.simpletron.core.component.CPU) : com.logickoder.simpletron.core.Simpletron(cpu) {
    var isRunning: Boolean = false

    var contracts = listOf(
        InputInstructionsContract(),
        ProgramLoadedContract(),
        ExecuteInstructionsContract()
    )

    @Throws(IllegalStateException::class)
    override fun run(): Unit = with(cpu.controlUnit) {
        check(!isRunning) { "Simpletron is already started" }
        isRunning = true
        val cpuContractor = CPUContractor(cpu)
        contracts.distinctBy { it.name }.forEach { it.execute(cpuContractor) }
    }

    @Throws(IllegalStateException::class)
    override fun shutdown() = with(cpu.controlUnit) {
        check(isRunning) { "Simpletron is already shutdown" }
        isRunning = false
    }
}