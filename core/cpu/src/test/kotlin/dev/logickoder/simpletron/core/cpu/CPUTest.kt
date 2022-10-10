package dev.logickoder.simpletron.core.cpu

import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.display.DisplayType
import dev.logickoder.simpletron.core.input.Input
import dev.logickoder.simpletron.core.memory.Memory
import dev.logickoder.simpletron.core.memory.stopValue
import dev.logickoder.simpletron.core.stub.StubCpu
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
        cpu = CPU(Memory(), mock {}, mockInput())
        stubCpu = StubCpu()
    }

    @Test
    fun checkThatTheDefaultInstructionsAre17InNumber() {
        assertEquals(17, cpu.instructions.size)
    }

    @Test
    fun checkThatImplementingTheCPUInterfaceInAnotherPackageShowsTheInstructionsInThatPackage() {
        assertEquals(18, stubCpu.instructions.size)
        assertNotEquals(null, stubCpu.instructions.find { it.code == TEST_VALUE })
    }

    @Test
    fun checkThatTheDefaultRegistersAre5InNumber() {
        assertEquals(5, cpu.registers.size)
    }

    @Test
    fun checkThatImplementingTheCPUInterfaceInAnotherPackageShowsTheRegistersInThatPackage() {
        assertEquals(6, stubCpu.registers.size)
        assertNotEquals(null, stubCpu.registers.find { it.value == TEST_VALUE })
    }

    @Test
    fun checkThatNullIsReturnedIfTheInstructionDoesNotExist() {
        cpu.memory[0] = -99 * 100
        val result = cpu.execute()
        assertEquals(null, result)
    }

    @Test
    fun testError() {
        val memory = Memory()
        with(CPU(memory, Display(DisplayType.CommandLine(memory.stopValue.toInt())), mockInput())) {
            val ic = register<InstructionCounter>()
            error("Test Error")
            assertEquals(Halt.code(memory, ic.value), memory[ic.value].toInt())
            assertEquals(Halt, execute())
        }
    }

    companion object {
        internal const val TEST_VALUE = -99999

        internal fun mockInput() = mock<Input> {
            on { read() } doReturn ""
        }
    }
}