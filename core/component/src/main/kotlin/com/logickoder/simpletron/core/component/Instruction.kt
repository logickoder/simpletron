package com.logickoder.simpletron.core.component

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
    abstract fun execute(cpu: com.logickoder.simpletron.core.component.CPU)
}

