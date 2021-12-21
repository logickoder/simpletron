package com.jeffreyorazulike.simpletron.core.components

import com.jeffreyorazulike.simpletron.core.components.Registers.*
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
    fun getRegisters() = Registers.values().toList()

    /**
     * Any implementation of this method should always return the default
     * operations in addition to newly defined operations that the system can
     * handle
     *
     * @return an array of operations that this CPU handle
     */
    fun getOperations(): List<Operation> = listOf(Read())


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
        return getOperations().find{ op -> op.code == Registers.OperationCode.value }
    }
}