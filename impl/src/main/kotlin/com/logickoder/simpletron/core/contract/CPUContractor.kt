package com.logickoder.simpletron.core.contract

import com.logickoder.simpletron.core.component.CPU

/**
 * A [Contractor] that uses The [CPU] to execute contracts
 */
class CPUContractor(override val executor: CPU) : Contractor<CPU>