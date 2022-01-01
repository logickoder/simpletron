package com.jeffreyorazulike.simpletron.core

import com.jeffreyorazulike.simpletron.core.components.Display
import com.jeffreyorazulike.simpletron.core.components.Memory
import com.jeffreyorazulike.simpletron.core.utils.dump
import org.junit.Before
import org.junit.Test
import java.time.Instant
import kotlin.random.Random

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 01 at 7:40 AM
 *
 */
class MemoryUtilsTest {
    private lateinit var memory: Memory

    @Before
    fun setup() {
        memory = Memory.create()
    }

    @Test
    fun testMemoryDump() {
        val display = TestDisplay()
        val random = Random(Instant.now().epochSecond)
        for (i in 0 until memory.size - 400) {
            memory[i] = random.nextFloat()
        }
        memory.dump(display)
    }

    companion object {
        class TestDisplay : Display() {
            override val isClosed: Boolean = false
            override fun show(message: String?) {
                print(message)
            }

            override fun close() {}
        }
    }
}