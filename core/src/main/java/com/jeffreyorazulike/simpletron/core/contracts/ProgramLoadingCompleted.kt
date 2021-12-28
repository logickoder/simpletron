package com.jeffreyorazulike.simpletron.core.contracts

import com.jeffreyorazulike.simpletron.core.components.CPU

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 26 at 9:25 PM
 *
 * Gets invoked by [com.jeffreyorazulike.simpletron.core.Simpletron]
 */
interface ProgramLoadingCompleted: Contract {

    override fun execute(cpu: CPU) = with(cpu.controlUnit) {
        display.show(   "\n*** Program loading completed ***\n" +
                        "*** Program execution begins  ***\n")
        super.execute(cpu)
    }
}