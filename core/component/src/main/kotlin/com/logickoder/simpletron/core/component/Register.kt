package com.logickoder.simpletron.core.component

/**A component in the [CPU] that stores values from the [Memory]
 * and execution of [Instruction]s so that they can be easily accessed by the [CPU]
 *
 * @property value the current word the register holds
 * @property name the name of the register
 */
abstract class Register<T : Number> {

    abstract var value: T

    open val name: String = this::class.simpleName.toString()
}