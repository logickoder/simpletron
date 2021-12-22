package com.jeffreyorazulike.simpletron.core.components

import com.jeffreyorazulike.simpletron.core.components.Registers.*
import org.reflections.Reflections

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:30 AM
 *
 */
interface CPU {

    /**
     * Executes the next instruction
     *
     * @param memory A reference to the memory
     *
     * @return the operation that was executed
     */
    fun execute(memory: Memory): Operation?

    /**
     * Any implementation of this method should always return the default
     * registers in addition to newly defined registers in the system
     *
     * @return a list of registers that this CPU contains
     */
    fun getRegisters(): List<Register> {
        // retrieve any register defined in the package of any class that implements this interface
        val additionalRegisters = Reflections(this::class.java.packageName).getSubTypesOf(Register::class.java).apply {
            removeIf { it.name == "${CPU::class.java.packageName}.Registers" }
        }
        // merge both of them and return the registers
        return additionalRegisters.map { it.getDeclaredConstructor().newInstance() }.toMutableList().apply {
            addAll(Registers.values())
        }
    }

    /**
     * Any implementation of this method should always return the default
     * operations in addition to newly defined operations that the system can
     * handle
     *
     * @return a list of the operations that this CPU handle
     */
    fun getOperations(): List<Operation> {
        // retrieve all the operations defined in this package, which are the default operations
        val defaultOperations = Reflections(CPU::class.java.packageName).getSubTypesOf(Operation::class.java)
        // retrieve any operation defined in the package of any class that implements this interface
        val additionalOperations = Reflections(this::class.java.packageName).getSubTypesOf(Operation::class.java)
        // merge both of them and return the operations
        return defaultOperations.union(additionalOperations).map { it.getDeclaredConstructor().newInstance() }
    }

    
    companion object {
        /**
         *
         * @return the default implementation of the CPU
         */
        fun create(): CPU = CPUImpl()
    }
}

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21 at 3:45 AM
 *
 */
private class CPUImpl: CPU {

    private val _operations by lazy { getOperations() }

    override fun execute(memory: Memory): Operation? {
        // store the value from the current memory address to the instruction register
        InstructionRegister.value = memory[InstructionCounter.value]
        // update the instruction counter
        InstructionCounter.value = InstructionCounter.value + 1
        // store the operation code
        OperationCode.value = InstructionRegister.value / memory.size
        // store the operand
        Operand.value = InstructionRegister.value % memory.size
        // return the operation code
        return _operations.find{ op -> op.code == OperationCode.value }
    }
}