package dev.logickoder.simpletron.translator

import dev.logickoder.simpletron.translator.Subroutine.Companion.subroutine
import dev.logickoder.simpletron.translator.statement.End
import dev.logickoder.simpletron.translator.statement.Return
import dev.logickoder.simpletron.translator.statement.Statement
import dev.logickoder.simpletron.translator.syntax.SyntaxError
import dev.logickoder.simpletron.translator.syntax.syntaxError
import java.lang.reflect.Modifier
import java.util.regex.Pattern

/**
 *
 * Translator is the base class that defines programs to handle the conversion of Simpletron basic language to its
 * machine counterpart
 *
 * @property statements the list of statements this translator can parse
 * @property subroutines the subroutines contained in this program
 */
abstract class Translator {
    abstract val statements: List<Class<out Statement>>
    abstract val subroutines: List<Subroutine>

    /**
     * Converts a simpletron basic language program line to a statement
     *
     * @throws SyntaxError if a problem arises while parsing the lines
     */
    @Throws(SyntaxError::class)
    open fun extractStatement(index: Int, line: String): Statement {
        // parse the line to make sure it looks like a correct statement
        return LINE_PATTERN.matcher(line.trim().lowercase()).run {
            if (matches()) {
                val lineNumber = group(1).toInt()
                val keyword = group(2)
                val expression = group(3)?.trim() ?: ""
                // check if the statement exists, and it's not an abstract class, if it doesn't throw a syntax error
                val statement = statements.find { clazz ->
                    clazz.simpleName.startsWith(keyword, true) && Modifier.isAbstract(clazz.modifiers).not()
                } ?: throw "\"$keyword\" is not a valid keyword".syntaxError(index)
                try {
                    // uses the statement class to create a statement object
                    val constructor = statement.declaredConstructors.first()
                    when (constructor.parameterCount) {
                        1 -> constructor.newInstance(lineNumber)
                        else -> constructor.newInstance(lineNumber, expression)
                    } as Statement
                } catch (exception: IllegalArgumentException) {
                    // an exception is thrown if there was a problem while parsing the statement
                    throw (exception.message?.let {
                        it.ifBlank { "invalid $keyword expression." }
                    } ?: SyntaxError.UNKNOWN_ERROR).syntaxError(index)
                }
            } else {
                // throw a syntax error if the line wasn't constructed correctly
                throw "unable to parse \"$index\"".syntaxError(index)
            }
        }
    }

    /**
     * Inflates lines of text to a subroutine with their respective statements
     *
     * @throws SyntaxError if a problem occurred during inflation
     */
    @Throws(SyntaxError::class)
    open fun inflateStatements(lines: List<String>): List<Subroutine> {
        var subroutine: String? = null
        var lineNumber = -1
        var statements = mutableListOf<Statement>()
        val subroutines = mutableListOf<Subroutine>()
        fun submit() = subroutine?.let {
            // make sure that the last statement in every subroutine is a return or end statement
            statements.last().also { statement ->
                if (!(statement is End || statement is Return))
                    throw ("a subroutine must end with a return or end statement, " +
                            "but \"$it\" doesn't").syntaxError()
            }
            // add the previous subroutine to the list of subroutines
            subroutines += Subroutine(it, statements)
            // reset any variable in use
            lineNumber = -1
            subroutine = null
            statements = mutableListOf()
        }
        lines.forEachIndexed { index, line ->
            line.subroutine?.let { subName ->
                // tries to submit the subroutine even though this should fail if a subroutine is present to be submitted
                // because the subroutine does not have a return or end statement
                submit()
                subroutine = subName
                // check if this new subroutine was named after an old one
                if (subroutines.any { it.name == subroutine }) {
                    throw "subroutine \"$subroutine\" already exists".syntaxError(index)
                }
            } ?: run {
                // make sure every statement is contained in a subroutine
                if (subroutine == null) {
                    throw "every statement must be placed in a subroutine".syntaxError(index)
                }
                val statement = extractStatement(index, line)
                // checks whether the last line number gotten from a statement is lesser than the current one
                if (statement.lineNumber <= lineNumber)
                    throw "line Number \"${statement.lineNumber}\" cannot come after \"$lineNumber\"".syntaxError(index)
                lineNumber = statement.lineNumber
                statements += statement
                // the only subroutine submission call that should work successfully
                if (statement is End || statement is Return) submit()
            }
        }
        // tries to submit the subroutine even though this should fail if a subroutine is present to be submitted
        // because the subroutine does not have a return or end statement
        submit()
        // return the subroutines with the main subroutine coming first
        return buildList {
            // checks that the default subroutine is present, if not throws a syntax error
            add(
                subroutines.firstOrNull { it.name == Subroutine.DEFAULT }?.also {
                    // verifies that the default subroutine ends with an end statement
                    if (it.statements.last() !is End)
                        throw "the \"${Subroutine.DEFAULT}\" subroutine must end with an end statement".syntaxError()
                } ?: throw "there must be a \"${Subroutine.DEFAULT}\" subroutine in your program".syntaxError())
            addAll(subroutines.filter { it.name != Subroutine.DEFAULT })
        }
    }

    /**
     * Executes the program while getting its input from simpletron
     */
    abstract fun run()

    companion object {
        private val LINE_PATTERN: Pattern = Pattern.compile("(\\d+)\\s+([a-z]+)(\\s+[!-~\\s+]+)?")
    }
}