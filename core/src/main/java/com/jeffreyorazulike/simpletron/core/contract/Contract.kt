package com.jeffreyorazulike.simpletron.core.contract

import com.jeffreyorazulike.simpletron.core.components.CPU

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 26 at 9:27 PM
 *
 * Defines a contract that should be executed when an action occurs
 *
 * @property contract the contract to be invoked
 */
interface Contract {
    val contract: (CPU) -> Unit

    /**
     * Performs any operation and executes the contract
     * */
    fun execute(cpu: CPU){ contract.invoke(cpu) }
}