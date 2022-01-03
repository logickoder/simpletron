package com.jeffreyorazulike.simpletron.core.impl.component

import com.jeffreyorazulike.simpletron.core.component.Memory
import com.jeffreyorazulike.simpletron.core.impl.utils.dump
import com.jeffreyorazulike.simpletron.core.impl.utils.separator
import com.jeffreyorazulike.simpletron.core.impl.utils.stopValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.time.Instant
import kotlin.random.Random

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 25 at 3:08 PM
 */
class MemoryTest {
    private lateinit var memory: Memory
    private lateinit var mockMemory1: Memory
    private lateinit var mockMemory2: Memory

    @Before
    fun setUp() {
        memory = MemoryImpl()
        mockMemory1 = mock {
            on { maxWord } doReturn 9999f
            on { minWord } doReturn -9999f
            on { size } doReturn 100
        }
        mockMemory2 = mock {
            on { maxWord } doReturn 999f
            on { minWord } doReturn -999f
            on { size } doReturn 10
        }
    }

    @Test
    fun checkThatTheStopValueIsOneValueLargerThanTheMinWordAndItsAll9s(){
        assertEquals(-999999f, memory.stopValue())
        assertEquals(-99999f, mockMemory1.stopValue())
        assertEquals(-9999f, mockMemory2.stopValue())
    }

    @Test
    fun checkThatTheSeparatorWillCorrectlySeparateTheInstructionCodeAndOperand(){
        assertEquals(1000, memory.separator())
        assertEquals(100, mockMemory1.separator())
        assertEquals(10, mockMemory2.separator())
    }

    @Test
    fun checkThatCallingSetOnMemoryStoresTheValueInThatAddress(){
        memory[100] = 99
        assertEquals(99f, memory[100])
    }

    @Test
    fun throwsAnExceptionWhenCallingAnAddressThatIsLowerThan0(){
        assertThrows(ArrayIndexOutOfBoundsException::class.java){ memory[-1] }
    }

    @Test
    fun throwsAnExceptionWhenAssigningAValueToAnAddressThatIsLowerThan0(){
        assertThrows(ArrayIndexOutOfBoundsException::class.java){ memory.set(-1, -1) }
    }

    @Test
    fun throwsAnExceptionWhenCallingAnAddressThatIsEqualToOrLargerThanTheSizeOfTheMemory(){
        assertThrows(ArrayIndexOutOfBoundsException::class.java){ memory[memory.size] }
    }

    @Test
    fun throwsAnExceptionWhenAssigningAValueToAnAddressThatIsEqualToOrLargerThanTheSizeOfTheMemory(){
        assertThrows(ArrayIndexOutOfBoundsException::class.java){ memory.set(memory.size, -1) }
    }

    @Test
    fun throwsAnExceptionWhenStoringAValueLessThanMinWord(){
        assertThrows(IllegalArgumentException::class.java){
            memory.set(1, memory.minWord - 1)
        }
    }

    @Test
    fun throwsAnExceptionWhenStoringAValueGreaterThanMaxWord() {
        assertThrows(IllegalArgumentException::class.java) {
            memory.set(1, memory.maxWord + 1)
        }
    }

    @Test
    fun testMemoryDump() {
        val display = CommandlineDisplay(memory.stopValue().toInt())
        val random = Random(Instant.now().epochSecond)
        for (i in 0 until memory.size - 400) {
            memory[i] = random.nextFloat()
        }
        memory.dump(display)
    }
}