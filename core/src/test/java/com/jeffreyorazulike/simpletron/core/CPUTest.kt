package com.jeffreyorazulike.simpletron.core

import com.jeffreyorazulike.simpletron.core.components.*
import org.junit.Before
import org.junit.Test

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 7:02 AM
 *
 */
class CPUTest {
    private lateinit var cpu: CPU
    private lateinit var stubCpu: CPU

    @Before
    fun init(){
        cpu = CPU.create()
        stubCpu = StubCPU()
    }

    @Test
    fun checkThatTheDefaultOperationsAre17InNumber(){
        val operations = cpu.getOperations()
        assert(operations.size == 17)
    }

    @Test
    fun checkThatImplementingTheCPUInterfaceInAnotherPackageShowsTheOperationsInThatPackage(){
        val operations = stubCpu.getOperations()
        assert(operations.size == 18)
        assert(operations.find { it.code == TEST_VALUE } != null)
    }

    @Test
    fun checkThatTheDefaultRegistersAre5InNumber(){
        val registers = cpu.getRegisters()
        assert(registers.size == 5)
    }

    @Test
    fun checkThatImplementingTheCPUInterfaceInAnotherPackageShowsTheRegistersInThatPackage(){
        val registers = stubCpu.getRegisters()
        assert(registers.size == 6)
        assert(registers.find { it.value == TEST_VALUE } != null)
    }

    @Test
    fun checkThatTheCorrectOperationIsReturnedAfterExecution(){
        val memory = Memory.create()
        val operations = cpu.getOperations()
        operations.forEachIndexed { index, operation ->
            memory[index] = operation.code * memory.separator()
        }
        operations.forEach {
            assert(cpu.execute(memory) == it)
        }
    }

    @Test
    fun checkThatNullIsReturnedIfTheOperationDoesNotExist(){
        val memory = Memory.create()
        memory[0] = -99 * 100
        val result = cpu.execute(memory)
        assert(result == null)
    }

    class StubOp : Operation() {
        override val code = TEST_VALUE
        override fun execute(params: ComponentParam) {}
    }
    class StubRegister : Register {
        override var value = TEST_VALUE
        override val name = "Stub"
    }
    class StubCPU : CPU() {
        override fun execute(memory: Memory): Operation? = null
    }

    companion object {
        private const val TEST_VALUE = -99999
    }
}