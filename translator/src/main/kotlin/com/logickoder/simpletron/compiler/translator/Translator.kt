package com.logickoder.simpletron.compiler.translator

import com.logickoder.simpletron.compiler.translator.syntax.statements.statementFactories

/**
 *
 * Translator is the base class that defines programs to handle the conversion of Simpletron basic language to its
 * machine counterpart
 *
 * @property statements the list of statements this translator can parse
 * @property lines the lines of a program this translator is parsing
 */
abstract class Translator {
    open val statements by lazy { statementFactories() }
    abstract val lines: List<String>
}