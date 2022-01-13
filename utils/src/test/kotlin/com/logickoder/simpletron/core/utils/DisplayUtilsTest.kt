package com.logickoder.simpletron.core.utils

import com.logickoder.simpletron.core.component.*
import com.logickoder.simpletron.core.impl.component.CPUImpl
import com.logickoder.simpletron.core.impl.component.CommandlineDisplay
import com.logickoder.simpletron.core.impl.component.MemoryImpl
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.time.Instant
import kotlin.random.Random

/**
 *
 */
class DisplayUtilsTest {
    private lateinit var memory: Memory
    private lateinit var cpu: CPU
    private lateinit var display: Display

    @Before
    fun setUp() {
        memory = MemoryImpl()
        display = CommandlineDisplay(memory.stopValue().toInt())
        cpu = CPUImpl(memory, display, mock {
            on { read() } doReturn ""
        })
    }

    @Test
    fun testCpuDump() {
        val random = Random(Instant.now().epochSecond)
        cpu.registers.forEach { register ->
            (register as? Register<Float>)?.value = random.nextFloat()
            (register as? Register<Int>)?.value = random.nextInt()
        }
        cpu.dump()
    }

    @Test
    fun testMemoryDump() {
        val random = Random(Instant.now().epochSecond)
        for (i in 0 until memory.size - 400) {
            memory[i] = random.nextFloat()
        }
        memory.dump(display)
    }
}