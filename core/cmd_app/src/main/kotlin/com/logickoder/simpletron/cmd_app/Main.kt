package com.logickoder.simpletron.cmd_app

import com.logickoder.simpletron.core.SimpletronImpl
import com.logickoder.simpletron.core.component.*
import com.logickoder.simpletron.core.contract.CPUContractor
import com.logickoder.simpletron.core.contract.Contract
import com.logickoder.simpletron.core.contract.Contractor
import com.logickoder.simpletron.core.contract.contracts.ExecuteInstructionsContract
import com.logickoder.simpletron.core.contract.contracts.InputInstructionsContract
import com.logickoder.simpletron.core.contract.contracts.ProgramLoadedContract

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