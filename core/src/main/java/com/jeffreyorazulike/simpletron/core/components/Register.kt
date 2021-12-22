package com.jeffreyorazulike.simpletron.core.components

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 4:03 AM
 *
 * Defines a register in the [CPU]
 */
abstract class Register {
    /**
     * The value of the register
     * */
    var value: Int = 0

    /**
     * The name of the register
     * */
    abstract val name: String
}