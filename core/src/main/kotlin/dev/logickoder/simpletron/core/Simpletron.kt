package dev.logickoder.simpletron.core

import dev.logickoder.simpletron.core.contract.Contract
import dev.logickoder.simpletron.core.contract.ExecuteInstructionsContract
import dev.logickoder.simpletron.core.contract.InputInstructionsContract
import dev.logickoder.simpletron.core.contract.ProgramLoadedContract
import dev.logickoder.simpletron.core.cpu.CPU


/**
 * The simpletron machine
 *
 * @property isRunning true if the machine is running
 */
interface Simpletron {

    val cpu: CPU

    val isRunning: Boolean

    val contracts: List<Contract>

    /**
     * This is where all initializations and starting of components should take
     * place
     *
     * @throws IllegalStateException if the system is already running
     */
    @Throws(IllegalStateException::class)
    fun run()

    /**
     * This is where all components started in the boot method should be stopped
     *
     * @throws IllegalStateException if the system is already shutdown
     */
    @Throws(IllegalStateException::class)
    fun shutdown()

    companion object {
        operator fun invoke(
            cpu: CPU,
            contracts: List<Contract> = listOf(
                InputInstructionsContract,
                ProgramLoadedContract,
                ExecuteInstructionsContract
            )
        ): Simpletron = SimpletronImpl(cpu, contracts)
    }
}