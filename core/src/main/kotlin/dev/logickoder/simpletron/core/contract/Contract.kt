package dev.logickoder.simpletron.core.contract

import dev.logickoder.simpletron.core.cpu.CPU
import dev.logickoder.simpletron.core.display.Display
import java.util.*

/**
 * Defines a contract that should be executed
 *
 * @property name the name of this contract, it should be unique as that is how contracts are differentiated, default is the fully qualified name of the class
 */
sealed class Contract<T> {

    open val name: String = this::class.qualifiedName ?: ""

    /**
     * Executes this contract
     * */
    abstract fun execute(contractor: T)

    override fun equals(other: Any?) = other is Contract<*> && other.name == name

    override fun hashCode() = Objects.hash(name)
}

/**
 * Contracts that are executed by the CPU
 */
abstract class CpuContract : Contract<CPU>()

/**
 * Contracts that are executed by the Display
 */
abstract class DisplayContract : Contract<Display>()