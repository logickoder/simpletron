package com.logickoder.simpletron.compiler.translator.syntax.statements

import com.logickoder.simpletron.compiler.translator.syntax.Statement

/**
 * A factory for creating statements, any statement must have its factory class,
 * because that's the only way the statement can be created.
 *
 * @property statementName the name of the statement this factory instantiates,
 * the default statement name is the name of the overriding class without the factory
 * in lowercase letters
 */
abstract class StatementFactory {

    open val statementName =
        this::class.simpleName?.lowercase()?.replace("factory", "") ?: ""

    /**
     * Creates a statement
     *
     * @param lineNumber the line number of this statement
     * @param line the action this statement is to perform
     * */
    abstract fun create(lineNumber: Int, line: String): Statement
}