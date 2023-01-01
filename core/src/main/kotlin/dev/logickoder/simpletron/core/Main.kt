package dev.logickoder.simpletron.core

import dev.logickoder.simpletron.core.contract.*
import dev.logickoder.simpletron.core.cpu.CPU
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.display.DisplayType
import dev.logickoder.simpletron.core.input.Input
import dev.logickoder.simpletron.core.input.InputType
import dev.logickoder.simpletron.core.memory.Memory
import dev.logickoder.simpletron.core.memory.stopValue
import java.io.File

fun main() {
    val memory = Memory()
    println(File("").absolutePath)
    val simpletron = Simpletron(
        cpu = CPU(
            memory = memory,
            display = Display(DisplayType.CommandLine(memory.stopValue.toInt())),
            input = Input(InputType.File("example programs\\sbl\\larger.sbl.sml")),
        ),
        contracts = listOf(
            InputInstructionsContract,
            ProgramLoadedContract,
            ChangeInputContract,
            ExecuteInstructionsContract
        )
    )
    simpletron.run()
}

private object ChangeInputContract : Contract() {
    override fun execute(contractor: Contractor<*>) {
        with((contractor as? CPUContractor)?.executor ?: return) {
            swap(Input(InputType.CommandLine))
        }
    }
}