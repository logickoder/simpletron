package com.jeffreyorazulike.simpletron.core.components

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 4:03 AM
 *
 * A component in the [CPU] that stores values from the [Memory]
 * and execution of [Instruction]s so that they can be easily accessed by the [CPU]
 *
 * @property value the current word the register holds
 * @property name the name of the register
 */
abstract class Register {

    var value: Int = 0

    open val name: String = this::class.simpleName.toString()
}