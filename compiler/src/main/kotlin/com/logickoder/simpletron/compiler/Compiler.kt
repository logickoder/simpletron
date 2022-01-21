package com.logickoder.simpletron.compiler

import com.logickoder.simpletron.compiler.statements.statementFactories
import com.logickoder.simpletron.core.Simpletron
import com.logickoder.simpletron.translator.Translator

/**
 *
 */
class Compiler(simpletron: Simpletron) : Translator(simpletron) {
    override val statements = statementFactories()
}