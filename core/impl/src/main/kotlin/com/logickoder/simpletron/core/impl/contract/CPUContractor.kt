package com.logickoder.simpletron.core.impl.contract

import com.jeffreyorazulike.simpletron.core.contract.Contractor
import com.logickoder.simpletron.core.component.CPU

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 03 at 5:29 PM
 *
 * A [Contractor] that uses The [CPU] to execute contracts
 */
class CPUContractor(override val executor: com.logickoder.simpletron.core.component.CPU) : Contractor<com.logickoder.simpletron.core.component.CPU>