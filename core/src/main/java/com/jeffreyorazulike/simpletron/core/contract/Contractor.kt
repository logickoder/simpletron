package com.jeffreyorazulike.simpletron.core.contract

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 26 at 9:33 PM
 *
 * Defines a list of contracts to be executed
 *
 * @property contracts the list of contracts to be executed
 */
interface Contractor {
    val contracts: List<Contract>
}