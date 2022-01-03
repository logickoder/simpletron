package com.jeffreyorazulike.simpletron.core.impl.contract

import com.jeffreyorazulike.simpletron.core.component.CPU
import com.jeffreyorazulike.simpletron.core.contract.Contractor

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 03 at 5:29 PM
 *
 * A [Contractor] that uses The [CPU] to execute contracts
 */
class CPUContractor(override val executor: CPU) : Contractor<CPU>