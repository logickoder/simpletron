package dev.logickoder.simpletron.translator.compiler.statement

import dev.logickoder.simpletron.translator.config.Config
import dev.logickoder.simpletron.translator.statement.Gosub

internal class GosubImpl(lineNumber: Int, subroutineName: String) : Gosub(lineNumber, subroutineName) {

    override fun <T : Config> translate(config: T) = buildList {
        with(config as CompilerConfig) {
            // execute the statements in the subroutine to be called
            val subroutine = subroutines.find { it.name == name }!!
            val newConfig = config.copy(subroutine = subroutine)
            subroutine.statements.forEach {
                addAll((it).translate(newConfig))
            }
            // clear the symbols of this subroutine from the table
            table.clear(subroutine)
        }
    }
}