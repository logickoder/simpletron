package dev.logickoder.simpletron.core.cpu.instructions

import dev.logickoder.simpletron.core.cpu.*
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.display.DisplayType
import dev.logickoder.simpletron.core.input.Input
import dev.logickoder.simpletron.core.memory.Memory
import dev.logickoder.simpletron.core.memory.stopValue
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

class InstructionsTest {

    private lateinit var cpu: CPU
    private var memoryLocation = 0
    private val out = ByteArrayOutputStream()
    private val originalOut = System.out

    private fun operand() = cpu.register<Operand>()
    private fun accumulator() = cpu.register<Accumulator>()
    private fun instructionCounter() = cpu.register<InstructionCounter>()

    @Before
    fun setUp() {
        val memory = Memory()
        cpu = CPUImpl(memory, Display(DisplayType.CommandLine(memory.stopValue.toInt())), mockInput())
    }

    @Before
    fun setUpStreams() {
        System.setOut(PrintStream(out))
    }

    @Test
    fun checkThatNewLineOutputsANewLine() {
        NewLine.execute(cpu, 0)
        assertEquals(cpu.display.newline, out.toString())
        NewLine.execute(cpu, 1)
        assertEquals("${cpu.display.newline}${cpu.display.newline}", out.toString())
    }

    @Test
    fun checkThatReadProducesIntegers() = with(cpu) {
        fun check(value: Int) {
            swap(mockInput("$value"))
            Read.execute(cpu, memoryLocation++)
            assertEquals(value.toFloat(), memory[operand().value])
        }
        check(0)
        check(11)
        check(22)
    }

    @Test
    fun checkThatReadCanHandleFloats() = with(cpu) {
        fun check(value: Float) {
            swap(mockInput("$value"))
            Read.execute(cpu, memoryLocation++)
            assertEquals(value, memory[operand().value])
        }
        check(11.2f)
        check(25.42f)
    }

    @Test
    fun checkThatWriteWorks() = with(cpu) {
        var string = ""
        fun check(value: Float) {
            memory[MEM_LOCATION] = value
            Write.execute(cpu, memoryLocation++)
            string += value
            assertEquals(string, out.toString())
        }
        check(11f)
        check(12f)
    }

    @Test
    fun checkThatReadStringWorks() = with(cpu) {
        fun check(string: String, operand: Int) {
            swap(mockInput(string))
            ReadString.execute(cpu, memoryLocation++, operand)
            for (i in string.indices) {
                assertEquals(string[i].code.toFloat(), memory[operand().value + i + 1])
            }
        }
        check("this is a string", 30)
        check("this is a second string", 50)
        check("this is a second string ooh", 60)
    }

    @Test
    fun checkThatWriteStringWorks() = with(cpu) {
        var strings = ""
        fun check(string: String, operand: Int) {
            memory[operand] = string.length
            for (i in 1..string.length)
                memory[operand + i] = string[i - 1].code

            WriteString.execute(cpu, memoryLocation++, operand)
            strings += string
            assertEquals(strings, out.toString())
        }
        check("this is a string", 30)
        check("this is a second string", 50)
    }

    @Test
    fun checkThatLoadWorks() = with(cpu) {
        fun check(value: Float) {
            memory[MEM_LOCATION] = value
            Load.execute(cpu, memoryLocation++)
            assertEquals(value, accumulator().value)
        }
        check(5895f)
        check(258f)
        check(2598f)
    }

    @Test
    fun checkThatStoreWorks() = with(cpu) {
        fun check(value: Float) {
            accumulator().value = value
            Store.execute(cpu, memoryLocation++)
            assertEquals(value, memory[MEM_LOCATION])
        }
        check(5895f)
        check(258f)
        check(2598f)
    }

    @Test
    fun checkThatAddWorks() = with(cpu) {
        fun check(first: Float, second: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first + second
            Add.execute(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }

        // checks that this operation will not work if overflow occurs
        fun check2(first: Float, second: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = second
            Add.execute(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
        }
        check(5895f, 589f)
        check2(90000f,20000f)
    }

    @Test
    fun checkThatSubtractWorks() = with(cpu) {
        fun check(first: Float, second: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first - second
            Subtract.execute(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }

        // checks that this operation will not work if overflow occurs
        fun check2(first: Float, second: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = second
            Subtract.execute(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
        }
        check(5895f, 589f)
        check2(-200000f,-3000f)
    }


    @Test
    fun checkThatDivideWorks() = with(cpu) {
        fun check(first: Float, second: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first / second
            Divide.execute(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }

        // attempt zero division
        fun check2(first: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = 0
            Divide.execute(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
            assertEquals("Attempt to divide by zero${cpu.display.newline}", out.toString())
        }
        check(90f, 100f)
        check2(20f)
    }

    @Test
    fun checkThatMultiplyWorks() = with(cpu) {
        fun check(first: Float, second: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first * second
            Multiply.execute(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }

        // checks that this operation will not work if overflow occurs
        fun check2(first: Float, second: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = second
            Multiply.execute(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
        }
        check(90f, 100f)
        check2(-200f,-3000f)
    }

    @Test
    fun checkThatRemainderWorks() = with(cpu) {
        fun check(first: Float, second: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first % second
            Remainder.execute(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }

        // attempt zero division
        fun check2(first: Float) {
            accumulator().value = first
            memory[MEM_LOCATION] = 0
            Remainder.execute(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
            assertEquals("Attempt to divide by zero${cpu.display.newline}", out.toString())
        }
        check(90f, 100f)
        check2(20f)
    }

    @Test
    fun checkThatExponentWorks() = with(cpu) {
        fun check(first: Float, second: Float) {
            accumulator().value = second
            memory[MEM_LOCATION] = first
            val result = first.toDouble().pow(second.toDouble()).toFloat()
            Exponent.execute(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }

        // checks that this operation will not work if overflow occurs
        fun check2(first: Float, second: Float) {
            accumulator().value = second
            memory[MEM_LOCATION] = first
            Exponent.execute(cpu, memoryLocation++)
            assertEquals(second, accumulator().value)
        }
        check(22f, 2f)
        check2(222f,3f)
    }

    @Test
    fun checkThatBranchWorks() = with(cpu) {
        fun check(operand: Int) {
            Branch.execute(cpu, memoryLocation++, operand)
            assertEquals(operand, instructionCounter().value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchNegWorks() = with(cpu) {
        fun check(operand: Int) {
            accumulator().value = (-operand).toFloat()
            BranchNeg.execute(cpu, memoryLocation++, operand)
            assertEquals(operand, instructionCounter().value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchNegDoesNotWorkIfAccumulatorIsPositive() = with(cpu) {
        fun check(operand: Int) {
            accumulator().value = operand.toFloat()
            BranchNeg.execute(cpu, memoryLocation++, operand)
            assertNotEquals(operand, instructionCounter().value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchZeroWorks() = with(cpu) {
        fun check(operand: Int) {
            accumulator().value = 0f
            BranchZero.execute(cpu, memoryLocation++, operand)
            assertEquals(operand, instructionCounter().value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchZeroDoesNotWorkIfAccumulatorIsNotZero() = with(cpu) {
        fun check(operand: Int) {
            accumulator().value = operand.toFloat()
            BranchZero.execute(cpu, memoryLocation++, operand)
            assertNotEquals(operand, instructionCounter().value)
        }
        check(22)
    }

    @After
    fun restoreStreams(){
        System.setOut(originalOut)
    }

    companion object {

        private const val MEM_LOCATION = 10

        private fun mockInput(input: String = "") = mock<Input> {
            on { read() } doReturn input
        }

        private fun Instruction.execute(
            cpu: CPU,
            memLocation: Int,
            operand: Int = MEM_LOCATION
        ) {
            cpu.memory[memLocation] = code(cpu.memory, operand)
            assertEquals(cpu.execute(), this)
        }
    }
}