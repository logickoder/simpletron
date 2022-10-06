package com.logickoder.simpletron.core

import com.logickoder.simpletron.core.component.CPU
import com.logickoder.simpletron.core.contract.CPUContractor
import com.logickoder.simpletron.core.contract.contracts.ExecuteInstructionsContract
import com.logickoder.simpletron.core.contract.contracts.InputInstructionsContract
import com.logickoder.simpletron.core.contract.contracts.ProgramLoadedContract

class SimpletronImpl(cpu: CPU) : Simpletron(cpu) {
    override var isRunning: Boolean = false

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