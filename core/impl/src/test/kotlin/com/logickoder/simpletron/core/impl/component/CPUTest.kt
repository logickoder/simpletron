package com.logickoder.simpletron.core.impl.component

import com.jeffreyorazulike.simpletron.core.impl.component.cpu.instructions.Halt
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.instructions.Read
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.registers.*
import com.jeffreyorazulike.simpletron.core.impl.utils.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 22 at 7:02 AM
 *
 */
class CPUTest {
    private lateinit var cpu: com.logickoder.simpletron.core.component.CPU
    private lateinit var stubCpu: com.logickoder.simpletron.core.component.CPU

    @Before
    fun init(){
        cpu = CPUImpl(MemoryImpl(), mock {}, mockInput())
        stubCpu = StubCPU()
    }

    @Test
    fun checkThatTheDefaultInstructionsAre17InNumber(){
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
    fun testDump() {
        cpu.register<Operand>().value = 370
        cpu.register<OperationCode>().value = 12
        cpu.register<InstructionCounter>().value = 1
        cpu.register<Accumulator>().value = 123f
        cpu.register<InstructionRegister>().value = 1245f
        cpu.changeComponent(display = CommandlineDisplay(cpu.controlUnit.memory.stopValue().toInt()))
        cpu.dump()
    }

    @Test
    fun testError() = with(cpu.controlUnit) {
        val ic = cpu.register<InstructionCounter>()
        cpu.changeComponent(display = CommandlineDisplay(memory.stopValue().toInt()))
        cpu.error("Test Error")
        assertEquals(Halt().code(memory, ic.value), memory[ic.value].toInt())
        assertEquals(Halt(), cpu.execute())
    }

    class StubOp : com.logickoder.simpletron.core.component.Instruction() {
        override val code = TEST_VALUE
        override fun execute(cpu: com.logickoder.simpletron.core.component.CPU) {}
    }

    class StubRegister : com.logickoder.simpletron.core.component.Register<Int>() {
        override var value = TEST_VALUE
        override val name = "Stub"
    }

    class StubCPU : com.logickoder.simpletron.core.component.CPU(MemoryImpl(), mock { }, mockInput()) {

        override val registers: List<com.logickoder.simpletron.core.component.Register<*>> = classInstances(
            setOf(
                Accumulator::class.java, StubCPU::class.java
            )
        )
        override val instructions: List<com.logickoder.simpletron.core.component.Instruction> = classInstances(
            setOf(
                Read::class.java, StubCPU::class.java
            )
        )

        override fun execute(): com.logickoder.simpletron.core.component.Instruction? = null
    }

    companion object {
        private const val TEST_VALUE = -99999

        private fun mockInput() = mock<com.logickoder.simpletron.core.component.Input> {
            on { read() } doReturn ""
        }
    }
}