package com.logickoder.simpletron.compiler

import com.logickoder.simpletron.compiler.statements.CompilableStatement
import com.logickoder.simpletron.compiler.statements.CompilerConfig
import com.logickoder.simpletron.compiler.statements.statementFactories
import com.logickoder.simpletron.core.Simpletron
import com.logickoder.simpletron.core.component.FileDisplay
import com.logickoder.simpletron.core.component.newline
import com.logickoder.simpletron.core.component.stopValue
import com.logickoder.simpletron.translator.Subroutine
import com.logickoder.simpletron.translator.Translator

/**
 * Compiles the program to simpletron machine language
 */
class Compiler(simpletron: Simpletron) : Translator(simpletron) {
    override val statementFactories = statementFactories()
    override val subroutines = mutableListOf<Subroutine>()

    override fun run(): Unit = with(simpletron.cpu) {
        // change the display to a file ending with sml
        changeComponent(display = FileDisplay(controlUnit.input.source.toString().plus(".sml")))
        // initialize the compiler configuration
        val table = SymbolTable.create(100)//controlUnit.memory.size)
        // inflate the input
        subroutines += inflateStatements(buildList {
            while (controlUnit.input.hasNext()) add(controlUnit.input.read())
        })
        // convert only the main subroutine since that is where the execution of the program is
        val machineCode = subroutines.first { it.name == Subroutine.DEFAULT }.let { main ->
            main.statements.flatMap {
                (it as CompilableStatement).compile(CompilerConfig(table, main, subroutines))
            }
        }.toMutableList()
        // resolve all flags
        table.flags.forEach { flag ->
            machineCode[flag.second] += table[flag.first]
        }
        // output the code to the connected display
        with(controlUnit) {
            machineCode.forEach { display.show("$it${newline()}") }
            display.show(memory.stopValue().toInt().toString())
            display.close()
        }
    }
}