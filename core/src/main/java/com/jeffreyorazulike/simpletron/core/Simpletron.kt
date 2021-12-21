package com.jeffreyorazulike.simpletron.core

import com.jeffreyorazulike.simpletron.core.components.ComponentParam

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 5:40 PM
 *
 */
interface Simpletron {

    /**
     * This is where all initializations and starting of components should take
     * place
     *
     * @param params the components to use
     *
     * @throws IllegalStateException if the system is already booted up
     */
    @Throws(IllegalStateException::class)
    fun boot(params: ComponentParam)

    fun enterCommands()

    /**
     * This is where all components started in the boot method should be stopped
     *
     * @throws IllegalStateException if the system is already shutdown
     */
    @Throws(IllegalStateException::class)
    fun shutdown()
}