package dev.logickoder.simpletron.core

import dev.logickoder.simpletron.core.contract.Contract
import dev.logickoder.simpletron.core.contract.ExecuteInstructionsContract
import dev.logickoder.simpletron.core.contract.InputInstructionsContract
import dev.logickoder.simpletron.core.contract.ProgramLoadedContract
import dev.logickoder.simpletron.core.cpu.CPU


/**
 * The simpletron machine
 *
 * @property cpu the cpu of the machine
 * @property isRunning true if the machine is running
 * @property contracts the contracts that will be executed in sequence
 */
interface Simpletron {

    val cpu: CPU

    val isRunning: Boolean

    val contracts: LinkedHashSet<Contract<*>>

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
            contracts: LinkedHashSet<Contract<*>> = linkedSetOf(
                InputInstructionsContract,
                ProgramLoadedContract,
                ExecuteInstructionsContract
            )
        ): Simpletron = SimpletronImpl(cpu, contracts)
    }
}