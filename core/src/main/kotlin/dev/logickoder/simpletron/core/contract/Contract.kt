package dev.logickoder.simpletron.core.contract

import java.util.*

/**
 * Defines a contract that should be executed
 *
 * @property name the name of this contract, it should be unique as that is how contracts are differentiated, default is the fully qualified name of the class
 */
abstract class Contract {

    open val name: String = this::class.qualifiedName ?: ""

    /**
     * Executes this contract
     * */
    abstract fun execute(contractor: Contractor<*>)

    override fun equals(other: Any?) = other is Contract && other.name == name

    override fun hashCode() = Objects.hash(name)
}