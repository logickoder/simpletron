package com.jeffreyorazulike.simpletron.cmd_app


import com.jeffreyorazulike.simpletron.core.component.Memory
import com.jeffreyorazulike.simpletron.core.contract.Contract
import com.jeffreyorazulike.simpletron.core.contract.Contractor
import com.jeffreyorazulike.simpletron.core.impl.SimpletronImpl
import com.jeffreyorazulike.simpletron.core.impl.component.*
import com.jeffreyorazulike.simpletron.core.impl.contract.CPUContractor
import com.jeffreyorazulike.simpletron.core.impl.contract.contracts.ExecuteInstructionsContract
import com.jeffreyorazulike.simpletron.core.impl.contract.contracts.InputInstructionsContract
import com.jeffreyorazulike.simpletron.core.impl.contract.contracts.ProgramLoadedContract
import com.jeffreyorazulike.simpletron.core.impl.utils.stopValue

fun main() {
    val memory: Memory = MemoryImpl()
    SimpletronImpl(
        CPUImpl(
            memory = memory,
            display = CommandlineDisplay(memory.stopValue().toInt()),
            input = FileInput("example programs\\sml\\test.sml")
        )
    ).apply {
        contracts = listOf(
            InputInstructionsContract(),
            ProgramLoadedContract(),
            ChangeInputContract(),
            ExecuteInstructionsContract()
        )
    }.run()
}

class ChangeInputContract : Contract() {
    override fun execute(contractor: Contractor<*>) {
        val cpu = (contractor as? CPUContractor)?.executor ?: return
        cpu.changeComponent(input = CommandlineInput())
    }
}