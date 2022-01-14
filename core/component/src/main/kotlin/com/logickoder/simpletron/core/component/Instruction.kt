package com.logickoder.simpletron.core.component

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
    abstract fun execute(cpu: CPU)

    override fun equals(other: Any?) = other is Instruction && other.code == code

    override fun hashCode() = Objects.hash(code)
}

