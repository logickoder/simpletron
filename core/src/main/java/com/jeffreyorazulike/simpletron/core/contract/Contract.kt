package com.jeffreyorazulike.simpletron.core.contract

import com.jeffreyorazulike.simpletron.core.components.CPU

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 26 at 9:27 PM
 *
 * Defines a contract that should be executed
 *
 * The name of a contract should be unique as that is how it will be differentiated from other contracts
 *
 * @property name the name of this contract, default is the fully qualified name of the class
 */
abstract class Contract {

    open val name: String = this::class.qualifiedName ?: ""

    /**
     * Executes this contract
     * */
    abstract fun execute(controlUnit: CPU.ControlUnit)
}