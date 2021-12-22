package com.jeffreyorazulike.simpletron.core.components

import java.util.*

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 4:13 AM
 *
 */
abstract class Operation {

    /**
     *
     * @return the code of this operation
     */
    abstract val code: Int

    /**
     * Executes this operation
     */
    abstract fun execute(params: ComponentParam)

    override fun equals(other: Any?) = other is Operation && other.code == code
    override fun hashCode() = Objects.hash(code)
}

