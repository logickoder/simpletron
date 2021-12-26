package com.jeffreyorazulike.simpletron.cmd_app

import com.jeffreyorazulike.simpletron.core.Simpletron
import com.jeffreyorazulike.simpletron.core.components.*
import com.jeffreyorazulike.simpletron.impl.CommandlineDisplay
import com.jeffreyorazulike.simpletron.impl.CommandlineInput
import com.jeffreyorazulike.simpletron.impl.SimpletronImpl

fun main(){
    val memory = Memory.create()
    val display: Display = CommandlineDisplay(memory.stopValue())
    val input: Input = CommandlineInput()
    val cpu = CPU.create(memory, display, input)
    val simpletron: Simpletron = SimpletronImpl(cpu)
//    simpletron.contracts += object : ProgramLoadingCompleted {
//        override val contract: (CPU) -> Unit = {
//            // change the input to commandline after loading the program from file
////            it.controlUnit = CPU.ControlUnit(memory, display, CommandlineInput())
//        }
//    }
    simpletron.run()
}