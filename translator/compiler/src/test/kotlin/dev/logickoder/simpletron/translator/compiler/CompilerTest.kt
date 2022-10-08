package dev.logickoder.simpletron.translator.compiler

import dev.logickoder.simpletron.core.Simpletron
import dev.logickoder.simpletron.core.cpu.CPU
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.display.DisplayType
import dev.logickoder.simpletron.core.input.Input
import dev.logickoder.simpletron.core.input.InputType
import dev.logickoder.simpletron.core.memory.Memory
import dev.logickoder.simpletron.core.memory.stopValue
import org.junit.Before
import org.junit.Test

class CompilerTest {
    private lateinit var compiler: Compiler

    @Before
    fun setup() {
        val memory = Memory()
        compiler = Compiler(
            Simpletron(
                CPU(
                    memory,
                    Display(DisplayType.CommandLine(memory.stopValue.toInt())),
                    Input(InputType.File("..\\..\\example programs\\sbl\\larger.sbl"))
                )
            )
        )
    }

    @Test
    fun run() {
        compiler.run()
    }
}