package com.jeffreyorazulike.simpletron.core

import com.jeffreyorazulike.simpletron.core.components.CPU
import com.jeffreyorazulike.simpletron.core.contract.Contract
import com.jeffreyorazulike.simpletron.core.contract.Contractor

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 5:40 PM
 *
 * The simpletron machine
 */
abstract class Simpletron(val cpu: CPU) : Contractor {

    abstract override var contracts: List<Contract>

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