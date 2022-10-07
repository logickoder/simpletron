package dev.logickoder.simpletron.core

import dev.logickoder.simpletron.core.contract.*
import dev.logickoder.simpletron.core.cpu.CPU

internal class SimpletronImpl(
    override val cpu: CPU,
    override val contracts: List<Contract>
) : Simpletron {

    override var isRunning: Boolean = false

    @Throws(IllegalStateException::class)
    override fun run(): Unit = with(cpu) {
        check(!isRunning) { "Simpletron is already started" }
        isRunning = true
        val cpuContractor = CPUContractor(cpu)
        contracts.distinctBy { it.name }.forEach { it.execute(cpuContractor) }
    }

    @Throws(IllegalStateException::class)
    override fun shutdown() = with(cpu) {
        check(isRunning) { "Simpletron is already shutdown" }
        isRunning = false
    }
}