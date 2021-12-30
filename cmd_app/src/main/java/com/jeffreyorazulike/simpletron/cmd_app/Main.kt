package com.jeffreyorazulike.simpletron.cmd_app

import com.jeffreyorazulike.simpletron.core.components.CPU
import com.jeffreyorazulike.simpletron.core.components.Memory
import com.jeffreyorazulike.simpletron.core.components.stopValue
import com.jeffreyorazulike.simpletron.core.contract.Contract
import com.jeffreyorazulike.simpletron.core.contract.ExecuteInstructionsContract
import com.jeffreyorazulike.simpletron.core.contract.InputInstructionsContract
import com.jeffreyorazulike.simpletron.core.contract.ProgramLoadedContract
import com.jeffreyorazulike.simpletron.impl.CommandlineDisplay
import com.jeffreyorazulike.simpletron.impl.CommandlineInput
import com.jeffreyorazulike.simpletron.impl.FileInput
import com.jeffreyorazulike.simpletron.impl.SimpletronImpl

fun main() {
    val memory = Memory.create()
    SimpletronImpl(
        CPU.create(
            memory = memory,
            display = CommandlineDisplay(memory.stopValue().toInt()),
            input = FileInput("example programs\\sml\\test.sml")
        )
    ).apply {
        contracts = listOf(
            InputInstructionsContract(),
            ProgramLoadedContract(),
            ChangeInputContract(cpu),
            ExecuteInstructionsContract(cpu)
        )
    }.run()
}

class ChangeInputContract(private val cpu: CPU) : Contract() {
    override fun execute(controlUnit: CPU.ControlUnit) {
        cpu.changeComponent(input = CommandlineInput())
    }
}