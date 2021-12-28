package com.jeffreyorazulike.simpletron.core

import com.jeffreyorazulike.simpletron.core.components.Memory
import com.jeffreyorazulike.simpletron.core.components.separator
import com.jeffreyorazulike.simpletron.core.components.stopValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

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
        memory = Memory.create()
        mockMemory1 = mock {
            on { maxWord } doReturn 9999
            on { minWord } doReturn -9999
            on { size } doReturn 100
        }
        mockMemory2 = mock {
            on { maxWord } doReturn 999
            on { minWord } doReturn -999
            on { size } doReturn 10
        }
    }

    @Test
    fun checkThatTheStopValueIsOneValueLargerThanTheMinWordAndItsAll9s(){
        assertEquals(-999999, memory.stopValue())
        assertEquals(-99999, mockMemory1.stopValue())
        assertEquals(-9999, mockMemory2.stopValue())
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
        assertEquals(99, memory[100])
    }

    @Test
    fun throwsAnExceptionWhenCallingAnAddressThatIsLowerThan0(){
        assertThrows(ArrayIndexOutOfBoundsException::class.java){ memory[-1] }
    }

    @Test
    fun throwsAnExceptionWhenAssigningAValueToAnAddressThatIsLowerThan0(){
        assertThrows(ArrayIndexOutOfBoundsException::class.java){ memory[-1] = -1 }
    }

    @Test
    fun throwsAnExceptionWhenCallingAnAddressThatIsEqualToOrLargerThanTheSizeOfTheMemory(){
        assertThrows(ArrayIndexOutOfBoundsException::class.java){ memory[memory.size] }
    }

    @Test
    fun throwsAnExceptionWhenAssigningAValueToAnAddressThatIsEqualToOrLargerThanTheSizeOfTheMemory(){
        assertThrows(ArrayIndexOutOfBoundsException::class.java){ memory[memory.size] = -1 }
    }

    @Test
    fun throwsAnExceptionWhenStoringAValueLessThanMinWord(){
        assertThrows(IllegalArgumentException::class.java){
            memory[1] = memory.minWord - 1
        }
    }

    @Test
    fun throwsAnExceptionWhenStoringAValueGreaterThanMaxWord(){
        assertThrows(IllegalArgumentException::class.java){
            memory[1] = memory.maxWord + 1
        }
    }
}