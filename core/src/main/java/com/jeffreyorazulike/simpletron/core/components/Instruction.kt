package com.jeffreyorazulike.simpletron.core.components

import java.util.*

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 4:13 AM
 *
 * Defines an action in simpletron
 *
 * @property code the integer representation of this instruction
 */
abstract class Instruction {

    abstract val code: Int

    /**
     * Executes this operation
     */
    abstract fun execute(controlUnit: CPU.ControlUnit)

    override fun equals(other: Any?) = other is Instruction && other.code == code
    override fun hashCode() = Objects.hash(code)
}

