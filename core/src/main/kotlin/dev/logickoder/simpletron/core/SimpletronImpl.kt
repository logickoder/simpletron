package dev.logickoder.simpletron.core

import dev.logickoder.simpletron.core.contract.*
import dev.logickoder.simpletron.core.cpu.CPU

internal class SimpletronImpl(
    override val cpu: CPU,
    override val contracts: LinkedHashSet<Contract<*>>
) : Simpletron {

    override var isRunning: Boolean = false

    @Throws(IllegalStateException::class)
    override fun run() {
        check(!isRunning) { "Simpletron is already started" }
        isRunning = true
        contracts.forEach { contract ->
            when (contract) {
                is CpuContract -> contract.execute(cpu)
                is DisplayContract -> contract.execute(cpu.display)
            }
        }
    }

    @Throws(IllegalStateException::class)
    override fun shutdown() {
        check(isRunning) { "Simpletron is already shutdown" }
        isRunning = false
    }
}