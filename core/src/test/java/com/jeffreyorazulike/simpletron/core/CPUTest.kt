package com.jeffreyorazulike.simpletron.core

import com.jeffreyorazulike.simpletron.core.components.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
        assertEquals("Checking for the amount of default operations in the CPU",17, operations.size)
    }

    @Test
    fun checkThatImplementingTheCPUInterfaceInAnotherPackageShowsTheOperationsInThatPackage(){
        val operations = stubCpu.getOperations()
        assertEquals("Expected 18 operations",18, operations.size)
        assertNotEquals("Expected null",null, operations.find { it.code == TEST_VALUE })
    }

    @Test
    fun checkThatTheDefaultRegistersAre5InNumber(){
        val registers = cpu.getRegisters()
        assertEquals("Checking for the amount of default registers in the CPU",5, registers.size)
    }

    @Test
    fun checkThatImplementingTheCPUInterfaceInAnotherPackageShowsTheRegistersInThatPackage(){
        val registers = stubCpu.getRegisters()
        assertEquals("Expected 6 registers",6, registers.size)
        assertNotEquals("Expected null",null, registers.find { it.value == TEST_VALUE })
    }

    @Test
    fun checkThatTheCorrectOperationIsReturnedAfterExecution(){
        val memory = Memory.create()
        val operations = cpu.getOperations()
        operations.forEachIndexed { index, operation ->
            memory[index] = operation.code * memory.separator()
        }
        operations.forEach {
            assertEquals("Expected ${it.javaClass.simpleName} operation", it, cpu.execute(memory))
        }
    }

    @Test
    fun checkThatNullIsReturnedIfTheOperationDoesNotExist(){
        val memory = Memory.create()
        memory[0] = -99 * 100
        val result = cpu.execute(memory)
        assertEquals("Expected the operation from execute to be null",null, result)
    }

    class StubOp : Operation() {
        override val code = TEST_VALUE
        override fun execute(params: ComponentParam) {}
    }
    class StubRegister : Register() {
        init {
            value = TEST_VALUE
        }
        override val name = "Stub"
    }
    class StubCPU : CPU() {
        override fun execute(memory: Memory): Operation? = null
    }

    companion object {
        private const val TEST_VALUE = -99999
    }
}