package com.jeffreyorazulike.simpletron.core

import com.jeffreyorazulike.simpletron.core.components.*
import com.jeffreyorazulike.simpletron.core.utils.code
import com.jeffreyorazulike.simpletron.core.utils.dump
import com.jeffreyorazulike.simpletron.core.utils.findRegister
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
    private lateinit var cpu: CPU
    private lateinit var stubCpu: CPU

    @Before
    fun init(){
        cpu = CPU.create(Memory.create(), mock{}, mockInput())
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
    fun checkThatAllInstructionsAreExecuted() = with(cpu) {
        val instructions = instructions.toMutableList().run {
            // remove all branch instructions
            removeIf { it.code in 40..49 }
            toSortedSet(Comparator.comparing { it.code })
        }.apply{
            forEachIndexed { index, instruction ->
                controlUnit.memory[index] = instruction.code(controlUnit.memory, 0)
            }
        }
        val executedInstructions = mutableSetOf<Instruction>()
        do {
            execute()?.let { executedInstructions.add(it) }
        }while (instructions.size != executedInstructions.size)
        assertEquals(instructions.size, executedInstructions.size)
    }

    @Test
    fun checkThatNullIsReturnedIfTheInstructionDoesNotExist() {
        cpu.controlUnit.memory[0] = -99 * 100
        val result = cpu.execute()
        assertEquals(null, result)
    }

    @Test
    fun testDump() {
        cpu.registers.findRegister<Operand>().value = 370
        cpu.registers.findRegister<OperationCode>().value = 12
        cpu.registers.findRegister<InstructionCounter>().value = 1
        cpu.registers.findRegister<Accumulator>().value = 123f
        cpu.registers.findRegister<InstructionRegister>().value = 1245f
        cpu.changeComponent(display = TestDisplay())
        cpu.dump()
    }

    class StubOp : Instruction() {
        override val code = TEST_VALUE
        override fun execute(cpu: CPU) {}
    }

    class StubRegister : Register<Int>() {
        override var value = TEST_VALUE
        override val name = "Stub"
    }

    class StubCPU : CPU(Memory.create(), mock { }, mockInput()) {
        override fun execute(): Instruction? = null
    }

    companion object {
        private const val TEST_VALUE = -99999

        class TestDisplay : Display() {
            override val isClosed: Boolean = false
            override fun show(message: String?) {
                print(message)
            }

            override fun close() {}
        }

        private fun mockInput() = mock<Input> {
            on { read() } doReturn ""
        }
    }
}