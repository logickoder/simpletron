package com.logickoder.simpletron.core.component

import com.logickoder.simpletron.core.instructions.Halt
import com.logickoder.simpletron.core.instructions.Read
import com.logickoder.simpletron.core.instructions.code
import com.logickoder.simpletron.core.instructions.error
import com.logickoder.simpletron.core.registers.Accumulator
import com.logickoder.simpletron.core.registers.InstructionCounter
import com.logickoder.simpletron.core.registers.register
import com.logickoder.simpletron.core.utils.classInstances
import com.logickoder.simpletron.core.utils.classes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class CPUTest {
    private lateinit var cpu: CPU
    private lateinit var stubCpu: CPU

    @Before
    fun init() {
        cpu = CPUImpl(MemoryImpl(), mock {}, mockInput())
        stubCpu = StubCPU()
    }

    @Test
    fun checkThatTheDefaultInstructionsAre17InNumber() {
        assertEquals(17, cpu.instructions.size)
    }

    @Test
    fun checkThatImplementingTheCPUInterfaceInAnotherPackageShowsTheInstructionsInThatPackage(){
        assertEquals(18, stubCpu.instructions.size)
        assertNotEquals(null, stubCpu.instructions.find { it.code == TEST_VALUE })
    }

    @Test
    fun checkThatTheDefaultRegistersAre5InNumber(){
        assertEquals(5, cpu.registers.size)
    }

    @Test
    fun checkThatImplementingTheCPUInterfaceInAnotherPackageShowsTheRegistersInThatPackage(){
        assertEquals(6, stubCpu.registers.size)
        assertNotEquals(null, stubCpu.registers.find { it.value == TEST_VALUE })
    }

    @Test
    fun checkThatNullIsReturnedIfTheInstructionDoesNotExist() {
        cpu.controlUnit.memory[0] = -99 * 100
        val result = cpu.execute()
        assertEquals(null, result)
    }

    @Test
    fun testError() = with(cpu.controlUnit) {
        val ic = cpu.register<InstructionCounter>()
        cpu.changeComponent(display = CommandlineDisplay(memory.stopValue().toInt()))
        cpu.error("Test Error")
        assertEquals(Halt().code(memory, ic.value), memory[ic.value].toInt())
        assertEquals(Halt(), cpu.execute())
    }

    class StubOp : Instruction() {
        override val code = TEST_VALUE
        override fun execute(cpu: CPU) {}
    }

    class StubRegister : Register<Int>() {
        override var value = TEST_VALUE
        override val name = "Stub"
    }

    class StubCPU : CPU(MemoryImpl(), mock { }, mockInput()) {

        override val registers: List<Register<*>> = classes<Register<*>>(
            setOf(Accumulator::class.java, StubCPU::class.java)
        ).classInstances()

        override val instructions: List<Instruction> = classes<Instruction>(
            setOf(Read::class.java, StubCPU::class.java)
        ).classInstances()

        override fun execute(): Instruction? = null
    }

    companion object {
        private const val TEST_VALUE = -99999

        private fun mockInput() = mock<Input> {
            on { read() } doReturn ""
        }
    }
}