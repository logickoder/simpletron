package com.logickoder.simpletron.compiler.translator

import com.logickoder.simpletron.compiler.translator.syntax.keyword.Keyword
import com.logickoder.simpletron.compiler.translator.syntax.keywords.Rem
import com.logickoder.simpletron.compiler.translator.utils.classes

/**
 *
 * Translator is the base class that defines programs to handle the conversion of Simpletron basic language to its
 * machine counterpart
 *
 * @property keywords the list of keywords this translator can parse
 * @property lines the lines of a program this translator is parsing
 */
abstract class Translator {
    open val keywords by lazy { classes<Keyword>(setOf(Rem::class.java)) }
    abstract val lines: List<String>
}