package com.logickoder.simpletron.compiler

import com.logickoder.simpletron.compiler.statements.Compilable
import com.logickoder.simpletron.compiler.statements.statementFactories
import com.logickoder.simpletron.core.Simpletron
import com.logickoder.simpletron.core.component.FileDisplay
import com.logickoder.simpletron.core.component.FileInput
import com.logickoder.simpletron.core.component.newline
import com.logickoder.simpletron.core.component.stopValue
import com.logickoder.simpletron.translator.Translator
import com.logickoder.simpletron.translator.symbol.Symbol
import com.logickoder.simpletron.translator.syntax.syntaxError

/**
 *
 */
class Compiler(simpletron: Simpletron) : Translator(simpletron) {
    override val statements = statementFactories()

    override fun run(pathToFile: String): Unit = with(simpletron.cpu) {
        // switch the simpletron input to a file input
        changeComponent(input = FileInput(pathToFile))
        changeComponent(display = FileDisplay("$pathToFile.sml"))
        var lastLineNumber = 0
        val table = SymbolTable.create(controlUnit.memory.size)
        val machineCode = mutableListOf<String>().apply {
            // read all the lines from the file
            while (controlUnit.input.hasNext)
                add(controlUnit.input.read())
            // convert all the lines to statements
        }.flatMapIndexed { index, line ->
            (extractStatement(index, line).also {
                if (lastLineNumber >= it.lineNumber)
                    throw "Line Number: \"${it.lineNumber}\" cannot come after \"$lastLineNumber\"".syntaxError(index)
                lastLineNumber = it.lineNumber
                table.add(Symbol.LineNumber(it.lineNumber))
            } as Compilable).compile(table, index)
        }.toMutableList()
        // resolve all flags
        table.flags.forEach { flag ->
            machineCode[flag.second] += table[flag.first]
        }
        with(controlUnit.display) {
            // output the code to the display
            machineCode.forEach { show("$it${newline()}") }
            show(controlUnit.memory.stopValue().toInt().toString())
            close()
        }
    }
}