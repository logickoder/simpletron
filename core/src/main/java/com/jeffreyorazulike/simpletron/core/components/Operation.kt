package com.jeffreyorazulike.simpletron.core.components

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 4:13 AM
 *
 */
interface Operation {

    /**
     *
     * @return the code of this operation
     */
    val code: Int

    /**
     * Executes this operation
     */
    fun execute(params: ComponentParam)
}

