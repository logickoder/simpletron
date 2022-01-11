package com.jeffreyorazulike.simpletron.compiler.translator.syntax

import com.jeffreyorazulike.simpletron.compiler.translator.syntax.keyword.Keyword

/**
 *
 * Holds the result of the parsing of a line by the translator, either an error or a keyword is created
 */
data class SyntaxResolve(
    val keyword: Keyword?,
    val error: SyntaxError?
) {
    val resolved = keyword != null
}
