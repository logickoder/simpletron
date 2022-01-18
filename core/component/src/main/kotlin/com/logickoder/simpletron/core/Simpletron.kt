package com.logickoder.simpletron.core

import com.logickoder.simpletron.core.component.CPU

/**
 * The simpletron machine
 *
 * @property isRunning true if the machine is running
 */
abstract class Simpletron(val cpu: CPU) {

    abstract val isRunning: Boolean

    /**
     * This is where all initializations and starting of components should take
     * place
     *
     * @throws IllegalStateException if the system is already running
     */
    @Throws(IllegalStateException::class)
    abstract fun run()

    /**
     * This is where all components started in the boot method should be stopped
     *
     * @throws IllegalStateException if the system is already shutdown
     */
    @Throws(IllegalStateException::class)
    abstract fun shutdown()
}