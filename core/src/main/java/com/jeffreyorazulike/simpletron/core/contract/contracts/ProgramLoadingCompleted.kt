package com.jeffreyorazulike.simpletron.core.contract.contracts

import com.jeffreyorazulike.simpletron.core.components.CPU
import com.jeffreyorazulike.simpletron.core.contract.Contract

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 26 at 9:25 PM
 *
 * Gets invoked by [com.jeffreyorazulike.simpletron.core.Simpletron]
 */
class ProgramLoadingCompleted(
    override val contract: (CPU) -> Unit = {}
) : Contract {

    override fun execute(cpu: CPU) = with(cpu.controlUnit) {
        display.show(
            "\n*** Program loading completed ***\n" +
                    "*** Program execution begins  ***\n"
        )
        super.execute(cpu)
    }
}