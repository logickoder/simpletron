package com.jeffreyorazulike.simpletron.core

import com.jeffreyorazulike.simpletron.core.components.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.math.pow

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 27 at 12:30 PM
 *
 */
class InstructionsTest {

    private lateinit var cpu: CPU
    private var memoryLocation = 0
    private val out = ByteArrayOutputStream()
    private val originalOut = System.out

    @Before
    fun setUp() {
        cpu = CPU.create(Memory.create(), TestDisplay(), mockInput())
    }

    @Before
    fun setUpStreams(){
        System.setOut(PrintStream(out))
    }

    @Test
    fun checkThatNewLineOutputsANewLine() {
        instruction<NewLine>(cpu, 0)
        assertEquals("\n", out.toString())
        instruction<NewLine>(cpu, 1)
        assertEquals("\n\n", out.toString())
    }

    @Test
    fun checkThatReadProducesIntegers() = with(cpu.controlUnit){
        fun check(value: Int){
            cpu.controlUnit = CPU.ControlUnit(memory, display, mockInput("$value"))
            instruction<Read>(cpu, memoryLocation++)
            assertEquals(value, memory[Operand.value])
        }
        check(0)
        check(11)
        check(22)
    }

    @Test
    fun checkThatReadCanHandleFloats() = with(cpu.controlUnit){
        fun check(value: Float){
            cpu.controlUnit = CPU.ControlUnit(memory, display, mockInput("$value"))
            instruction<Read>(cpu, memoryLocation++)
            assertEquals(value, memory[Operand.value])
        }
        check(11.2f)
        check(25.42f)
    }

    @Test
    fun checkThatWriteWorks() = with(cpu.controlUnit){
        var string = ""
        fun check(value: Int){
            memory[MEM_LOCATION] = value
            instruction<Write>(cpu, memoryLocation++)
            string += value
            assertEquals(string, out.toString())
        }
        check(11)
        check(12)
    }

    @Test
    fun checkThatReadStringWorks() = with(cpu.controlUnit){
        fun check(string: String, operand: Int){
            cpu.controlUnit = CPU.ControlUnit(memory, display, mockInput(string))
            instruction<ReadString>(cpu, memoryLocation++, operand)
            for(i in string.indices){
                assertEquals(string[i].code, memory[Operand.value + i + 1])
            }
        }
        check("this is a string", 30)
        check("this is a second string", 50)
        check("this is a second string ooh", 60)
    }

    @Test
    fun checkThatWriteStringWorks() = with(cpu.controlUnit){
        var strings = ""
        fun check(string: String, operand: Int){
            memory[operand] = string.length
            for (i in 1..string.length)
                memory[operand + i] = string[i - 1].code

            instruction<WriteString>(cpu, memoryLocation++, operand)
            strings += string
            assertEquals(strings, out.toString())
        }
        check("this is a string", 30)
        check("this is a second string", 50)
    }

    @Test
    fun checkThatLoadWorks() = with(cpu.controlUnit){
        fun check(value: Int){
            memory[MEM_LOCATION] = value
            instruction<Load>(cpu, memoryLocation++)
            assertEquals(value, Accumulator.value)
        }
        check(5895)
        check(258)
        check(2598)
    }

    @Test
    fun checkThatStoreWorks() = with(cpu.controlUnit){
        fun check(value: Int){
            Accumulator.value = value
            instruction<Store>(cpu, memoryLocation++)
            assertEquals(value, memory[MEM_LOCATION])
        }
        check(5895)
        check(258)
        check(2598)
    }

    @Test
    fun checkThatAddWorks() = with(cpu.controlUnit){
        fun check(first: Int, second: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = second
            val result = first + second
            instruction<Add>(cpu, memoryLocation++)
            assertEquals(result, Accumulator.value)
        }
        // checks that this operation will not work if overflow occurs
        fun check2(first: Int, second: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = second
            instruction<Add>(cpu, memoryLocation++)
            assertEquals(first, Accumulator.value)
        }
        check(5895, 589)
        check2(9000,2000)
    }

    @Test
    fun checkThatSubtractWorks() = with(cpu.controlUnit){
        fun check(first: Int, second: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = second
            val result = first - second
            instruction<Subtract>(cpu, memoryLocation++)
            assertEquals(result, Accumulator.value)
        }
        // checks that this operation will not work if overflow occurs
        fun check2(first: Int, second: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = second
            instruction<Subtract>(cpu, memoryLocation++)
            assertEquals(first, Accumulator.value)
        }
        check(5895, 589)
        check2(-20000,-3000)
    }


    @Test
    fun checkThatDivideWorks() = with(cpu.controlUnit){
        fun check(first: Int, second: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = second
            val result = first / second
            instruction<Divide>(cpu, memoryLocation++)
            assertEquals(result, Accumulator.value)
        }
        // attempt zero division
        fun check2(first: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = 0
            instruction<Divide>(cpu, memoryLocation++)
            assertEquals(first, Accumulator.value)
            assertEquals("Attempt to divide by zero\n", out.toString())
        }
        check(90, 100)
        check2(20)
    }

    @Test
    fun checkThatMultiplyWorks() = with(cpu.controlUnit){
        fun check(first: Int, second: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = second
            val result = first * second
            instruction<Multiply>(cpu, memoryLocation++)
            assertEquals(result, Accumulator.value)
        }
        // checks that this operation will not work if overflow occurs
        fun check2(first: Int, second: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = second
            instruction<Multiply>(cpu, memoryLocation++)
            assertEquals(first, Accumulator.value)
        }
        check(90, 100)
        check2(-20,-3000)
    }

    @Test
    fun checkThatRemainderWorks() = with(cpu.controlUnit){
        fun check(first: Int, second: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = second
            val result = first % second
            instruction<Remainder>(cpu, memoryLocation++)
            assertEquals(result, Accumulator.value)
        }
        // attempt zero division
        fun check2(first: Int){
            Accumulator.value = first
            memory[MEM_LOCATION] = 0
            instruction<Remainder>(cpu, memoryLocation++)
            assertEquals(first, Accumulator.value)
            assertEquals("Attempt to divide by zero\n", out.toString())
        }
        check(90, 100)
        check2(20)
    }

    @Test
    fun checkThatExponentWorks() = with(cpu.controlUnit){
        fun check(first: Int, second: Int){
            Accumulator.value = second
            memory[MEM_LOCATION] = first
            val result = first.toDouble().pow(second).toInt()
            instruction<Exponent>(cpu, memoryLocation++)
            assertEquals(result, Accumulator.value)
        }
        // checks that this operation will not work if overflow occurs
        fun check2(first: Int, second: Int){
            Accumulator.value = second
            memory[MEM_LOCATION] = first
            instruction<Exponent>(cpu, memoryLocation++)
            assertEquals(second, Accumulator.value)
        }
        check(22, 2)
        check2(22,3)
    }

    @Test
    fun checkThatBranchWorks() = with(cpu.controlUnit){
        fun check(operand: Int){
            instruction<Branch>(cpu, memoryLocation++, operand)
            assertEquals(operand, InstructionCounter.value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchNegWorks() = with(cpu.controlUnit){
        fun check(operand: Int){
            Accumulator.value = -operand
            instruction<BranchNeg>(cpu, memoryLocation++, operand)
            assertEquals(operand, InstructionCounter.value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchNegDoesNotWorkIfAccumulatorIsPositive() = with(cpu.controlUnit){
        fun check(operand: Int){
            Accumulator.value = operand
            instruction<BranchNeg>(cpu, memoryLocation++, operand)
            assertNotEquals(operand, InstructionCounter.value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchZeroWorks() = with(cpu.controlUnit){
        fun check(operand: Int){
            Accumulator.value = 0
            instruction<BranchZero>(cpu, memoryLocation++, operand)
            assertEquals(operand, InstructionCounter.value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchZeroDoesNotWorkIfAccumulatorIsNotZero() = with(cpu.controlUnit){
        fun check(operand: Int){
            Accumulator.value = operand
            instruction<BranchZero>(cpu, memoryLocation++, operand)
            assertNotEquals(operand, InstructionCounter.value)
        }
        check(22)
    }

    @After
    fun restoreStreams(){
        System.setOut(originalOut)
    }

    companion object {

        class TestDisplay: Display() {
            override val isClosed: Boolean = false
            override fun show(message: String?) {
                print(message)
            }
            override fun close() {}
        }

        private const val MEM_LOCATION = 10

        private fun mockInput(input: String = "") = mock<Input>{
            on { read() } doReturn input
        }
        private inline fun <reified T: Instruction> instruction(cpu: CPU, memLocation: Int, operand: Int = MEM_LOCATION) {
            val instruction = cpu.instructions.filterIsInstance<T>().first()
            cpu.controlUnit.memory[memLocation] = instruction.code(cpu.controlUnit.memory, operand)
            assert(cpu.execute() is T)
        }
    }
}