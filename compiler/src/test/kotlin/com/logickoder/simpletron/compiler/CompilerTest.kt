package com.logickoder.simpletron.compiler

import com.logickoder.simpletron.core.SimpletronImpl
import com.logickoder.simpletron.core.component.*
import org.junit.Before
import org.junit.Test

/**
 *
 */
class CompilerTest {
    private lateinit var compiler: Compiler

    @Before
    fun setup() {
        val memory = MemoryImpl()
        compiler = Compiler(
            SimpletronImpl(
                CPUImpl(
                    MemoryImpl(),
                    CommandlineDisplay(memory.stopValue().toInt()),
                    FileInput("E:\\compile.txt")
                )
            )
        )
    }

    @Test
    fun run() {
        compiler.run()
    }
}