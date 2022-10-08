package dev.logickoder.simpletron.translator.compiler

import dev.logickoder.simpletron.core.Simpletron
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.display.DisplayType
import dev.logickoder.simpletron.core.memory.stopValue
import dev.logickoder.simpletron.translator.Subroutine
import dev.logickoder.simpletron.translator.Translator
import dev.logickoder.simpletron.translator.compiler.statements.CompilableStatement
import dev.logickoder.simpletron.translator.compiler.statements.CompilerConfig
import dev.logickoder.simpletron.translator.compiler.statements.statementFactories

/**
 * Compiles the program to simpletron machine language
 */
class Compiler(simpletron: Simpletron) : Translator(simpletron) {
    override val statementFactories = statementFactories()
    override val subroutines = mutableListOf<Subroutine>()

    override fun run(): Unit = with(simpletron.cpu) {
        // change the display to a file ending with sml
        swap(Display(DisplayType.File(input.source.toString().plus(".sml"))))
        // initialize the compiler configuration
        val table = SymbolTable(100)//controlUnit.memory.size)
        // inflate the input
        subroutines += inflateStatements(buildList {
            while (input.hasNext()) add(input.read())
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
        machineCode.forEach { display.show("$it${display.newline}") }
        display.show(memory.stopValue.toInt().toString())
        display.close()
    }
}