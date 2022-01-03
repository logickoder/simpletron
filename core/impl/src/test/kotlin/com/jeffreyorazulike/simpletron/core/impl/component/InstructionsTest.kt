package com.jeffreyorazulike.simpletron.core.impl.component

import com.jeffreyorazulike.simpletron.core.component.CPU
import com.jeffreyorazulike.simpletron.core.component.Display
import com.jeffreyorazulike.simpletron.core.component.Input
import com.jeffreyorazulike.simpletron.core.component.Instruction
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.instructions.*
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.registers.Accumulator
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.registers.InstructionCounter
import com.jeffreyorazulike.simpletron.core.impl.component.cpu.registers.Operand
import com.jeffreyorazulike.simpletron.core.impl.utils.code
import com.jeffreyorazulike.simpletron.core.impl.utils.newline
import com.jeffreyorazulike.simpletron.core.impl.utils.register
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

    private fun operand() = cpu.register<Operand>()
    private fun accumulator() = cpu.register<Accumulator>()
    private fun instructionCounter() = cpu.register<InstructionCounter>()

    @Before
    fun setUp() {
        cpu = CPUImpl(MemoryImpl(), TestDisplay(), mockInput())
    }

    @Before
    fun setUpStreams() {
        System.setOut(PrintStream(out))
    }

    @Test
    fun checkThatNewLineOutputsANewLine() {
        instruction<NewLine>(cpu, 0)
        assertEquals(newline(), out.toString())
        instruction<NewLine>(cpu, 1)
        assertEquals("${newline()}${newline()}", out.toString())
    }

    @Test
    fun checkThatReadProducesIntegers() = with(cpu.controlUnit){
        fun check(value: Int){
            cpu.changeComponent(input = mockInput("$value"))
            instruction<Read>(cpu, memoryLocation++)
            assertEquals(value.toFloat(), memory[operand().value])
        }
        check(0)
        check(11)
        check(22)
    }

    @Test
    fun checkThatReadCanHandleFloats() = with(cpu.controlUnit){
        fun check(value: Float){
            cpu.changeComponent(input = mockInput("$value"))
            instruction<Read>(cpu, memoryLocation++)
            assertEquals(value, memory[operand().value])
        }
        check(11.2f)
        check(25.42f)
    }

    @Test
    fun checkThatWriteWorks() = with(cpu.controlUnit){
        var string = ""
        fun check(value: Float){
            memory[MEM_LOCATION] = value
            instruction<Write>(cpu, memoryLocation++)
            string += value
            assertEquals(string, out.toString())
        }
        check(11f)
        check(12f)
    }

    @Test
    fun checkThatReadStringWorks() = with(cpu.controlUnit){
        fun check(string: String, operand: Int){
            cpu.changeComponent(input = mockInput(string))
            instruction<ReadString>(cpu, memoryLocation++, operand)
            for(i in string.indices){
                assertEquals(string[i].code.toFloat(), memory[operand().value + i + 1])
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
        fun check(value: Float){
            memory[MEM_LOCATION] = value
            instruction<Load>(cpu, memoryLocation++)
            assertEquals(value, accumulator().value)
        }
        check(5895f)
        check(258f)
        check(2598f)
    }

    @Test
    fun checkThatStoreWorks() = with(cpu.controlUnit){
        fun check(value: Float){
            accumulator().value = value
            instruction<Store>(cpu, memoryLocation++)
            assertEquals(value, memory[MEM_LOCATION])
        }
        check(5895f)
        check(258f)
        check(2598f)
    }

    @Test
    fun checkThatAddWorks() = with(cpu.controlUnit){
        fun check(first: Float, second: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first + second
            instruction<Add>(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }
        // checks that this operation will not work if overflow occurs
        fun check2(first: Float, second: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = second
            instruction<Add>(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
        }
        check(5895f, 589f)
        check2(90000f,20000f)
    }

    @Test
    fun checkThatSubtractWorks() = with(cpu.controlUnit){
        fun check(first: Float, second: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first - second
            instruction<Subtract>(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }
        // checks that this operation will not work if overflow occurs
        fun check2(first: Float, second: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = second
            instruction<Subtract>(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
        }
        check(5895f, 589f)
        check2(-200000f,-3000f)
    }


    @Test
    fun checkThatDivideWorks() = with(cpu.controlUnit){
        fun check(first: Float, second: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first / second
            instruction<Divide>(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }
        // attempt zero division
        fun check2(first: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = 0
            instruction<Divide>(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
            assertEquals("Attempt to divide by zero${newline()}", out.toString())
        }
        check(90f, 100f)
        check2(20f)
    }

    @Test
    fun checkThatMultiplyWorks() = with(cpu.controlUnit){
        fun check(first: Float, second: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first * second
            instruction<Multiply>(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }
        // checks that this operation will not work if overflow occurs
        fun check2(first: Float, second: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = second
            instruction<Multiply>(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
        }
        check(90f, 100f)
        check2(-200f,-3000f)
    }

    @Test
    fun checkThatRemainderWorks() = with(cpu.controlUnit){
        fun check(first: Float, second: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = second
            val result = first % second
            instruction<Remainder>(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }
        // attempt zero division
        fun check2(first: Float){
            accumulator().value = first
            memory[MEM_LOCATION] = 0
            instruction<Remainder>(cpu, memoryLocation++)
            assertEquals(first, accumulator().value)
            assertEquals("Attempt to divide by zero${newline()}", out.toString())
        }
        check(90f, 100f)
        check2(20f)
    }

    @Test
    fun checkThatExponentWorks() = with(cpu.controlUnit){
        fun check(first: Float, second: Float){
            accumulator().value = second
            memory[MEM_LOCATION] = first
            val result = first.toDouble().pow(second.toDouble()).toFloat()
            instruction<Exponent>(cpu, memoryLocation++)
            assertEquals(result, accumulator().value)
        }
        // checks that this operation will not work if overflow occurs
        fun check2(first: Float, second: Float){
            accumulator().value = second
            memory[MEM_LOCATION] = first
            instruction<Exponent>(cpu, memoryLocation++)
            assertEquals(second, accumulator().value)
        }
        check(22f, 2f)
        check2(222f,3f)
    }

    @Test
    fun checkThatBranchWorks() = with(cpu.controlUnit){
        fun check(operand: Int){
            instruction<Branch>(cpu, memoryLocation++, operand)
            assertEquals(operand, instructionCounter().value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchNegWorks() = with(cpu.controlUnit){
        fun check(operand: Int){
            accumulator().value = (-operand).toFloat()
            instruction<BranchNeg>(cpu, memoryLocation++, operand)
            assertEquals(operand, instructionCounter().value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchNegDoesNotWorkIfAccumulatorIsPositive() = with(cpu.controlUnit){
        fun check(operand: Int){
            accumulator().value = operand.toFloat()
            instruction<BranchNeg>(cpu, memoryLocation++, operand)
            assertNotEquals(operand, instructionCounter().value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchZeroWorks() = with(cpu.controlUnit){
        fun check(operand: Int){
            accumulator().value = 0f
            instruction<BranchZero>(cpu, memoryLocation++, operand)
            assertEquals(operand, instructionCounter().value)
        }
        check(22)
    }

    @Test
    fun checkThatBranchZeroDoesNotWorkIfAccumulatorIsNotZero() = with(cpu.controlUnit){
        fun check(operand: Int){
            accumulator().value = operand.toFloat()
            instruction<BranchZero>(cpu, memoryLocation++, operand)
            assertNotEquals(operand, instructionCounter().value)
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