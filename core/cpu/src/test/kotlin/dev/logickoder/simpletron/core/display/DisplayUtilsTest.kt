package dev.logickoder.simpletron.core.display

import dev.logickoder.simpletron.core.cpu.CPU
import dev.logickoder.simpletron.core.cpu.Register
import dev.logickoder.simpletron.core.cpu.dump
import dev.logickoder.simpletron.core.memory.Memory
import dev.logickoder.simpletron.core.memory.stopValue
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
        memory = Memory()
        display = Display(DisplayType.CommandLine(memory.stopValue.toInt()))
        cpu = CPU(memory, display, mock {
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