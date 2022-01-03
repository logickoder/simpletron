package com.jeffreyorazulike.simpletron.core.contract

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 26 at 9:33 PM
 *
 * Defines a list of contracts to be executed, contracts shouldn't be changed when this contractor is running
 *
 * @property isRunning true if this contractor has started executing contracts
 * @property contracts the list of contracts to be executed
 */
interface Contractor {
    val isRunning: Boolean
    val contracts: List<Contract>
}