package com.logickoder.simpletron.compiler.translator

import com.logickoder.simpletron.compiler.translator.syntax.Statement
import com.logickoder.simpletron.compiler.translator.syntax.statements.Rem
import com.logickoder.simpletron.compiler.translator.utils.classes

/**
 *
 * Translator is the base class that defines programs to handle the conversion of Simpletron basic language to its
 * machine counterpart
 *
 * @property statements the list of statements this translator can parse
 * @property lines the lines of a program this translator is parsing
 */
abstract class Translator {
    open val statements by lazy { classes<Statement>(setOf(Rem::class.java)) }
    abstract val lines: List<String>
}