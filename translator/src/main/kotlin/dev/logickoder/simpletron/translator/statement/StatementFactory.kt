package dev.logickoder.simpletron.translator.statement

/**
 * A factory for creating statements, any statement must have its factory class,
 * because that's the only way the statement can be created.
 *
 * @property keyword the name of the statement this factory instantiates
 */
abstract class StatementFactory<out T : Statement>(val keyword: String) {

    /**
     * Creates a statement
     *
     * @param lineNumber the line number of this statement
     * @param line the action this statement is to perform
     * */
    abstract fun create(lineNumber: Int, line: String): T
}