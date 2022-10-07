package dev.logickoder.simpletron.core.contract

import dev.logickoder.simpletron.core.cpu.CPU

/**
 * A [Contractor] that uses The [CPU] to execute contracts
 */
internal class CPUContractor(override val executor: CPU) : Contractor<CPU>