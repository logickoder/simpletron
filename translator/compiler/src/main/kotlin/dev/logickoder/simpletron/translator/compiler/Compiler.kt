package dev.logickoder.simpletron.translator.compiler

import dev.logickoder.simpletron.core.Simpletron
import dev.logickoder.simpletron.core.common.classes
import dev.logickoder.simpletron.core.common.hex
import dev.logickoder.simpletron.core.display.Display
import dev.logickoder.simpletron.core.display.DisplayType
import dev.logickoder.simpletron.core.memory.stopValue
import dev.logickoder.simpletron.translator.Subroutine
import dev.logickoder.simpletron.translator.Translator
import dev.logickoder.simpletron.translator.compiler.statement.CompilerConfig
import dev.logickoder.simpletron.translator.compiler.statement.RemImpl
import dev.logickoder.simpletron.translator.compiler.symbol.SymbolTable
import dev.logickoder.simpletron.translator.statement.Statement
import java.io.File

/**
 * Compiles the program to simpletron machine language
 */
class Compiler(simpletron: Simpletron) : Translator(simpletron) {
    override val statements = classes<Statement>(setOf(RemImpl::class.java))
    override val subroutines = mutableListOf<Subroutine>()

    override fun run(): Unit = with(simpletron.cpu) {
        val output = File(input.source.toString().plus(".sml"))
        // change the display to a file ending with sml
        swap(Display(DisplayType.File(output.path)))
        // initialize the compiler configuration
        val table = SymbolTable(100)//controlUnit.memory.size)
        // inflate the input
        try {
            subroutines += inflateStatements(buildList {
                while (input.hasNext()) add(input.read())
            })
        } catch (e: Exception) {
            input.close()
            // delete the output file if an error occurs
            output.delete()
            throw e
        }
        // convert only the main subroutine since that is where the execution of the program is
        val machineCode = buildList<Float> {
            subroutines.first { it.name == Subroutine.DEFAULT }.let { main ->
                addAll(
                    main.statements.flatMap { statement ->
                        statement.translate(CompilerConfig(table, main, subroutines)).map { it.toFloat() }
                    }
                )
            }
            // resolve all flags
            table.flags.forEach { (line, location) ->
                if (table[line] == SymbolTable.DOES_NOT_EXIST) {
                    table.add(line)
                }
                set(location, get(location) + table[line])
            }
        }

        // output the code to the connected display
        display.run {
            machineCode.forEach { show("$it$newline") }
            table.constants.forEach {
                // convert each constant to hex, so that they won't be tampered by the system
                show("${it.hex}$newline")
            }
            show(memory.stopValue.toInt().toString())
            close()
        }
    }
}