package dev.logickoder.simpletron.translator.statement

/**
 * Transfer program control to the specified line.
 *
 * Example: 70 goto 45
 *
 * @property line the number of the line to goto
 * */
abstract class Goto(override val lineNumber: Int, final override val action: String) : Statement {
    protected val line: Int

    init {
        require(action.toIntOrNull() != null) { "required line number, found \"$action\"" }
        line = action.toInt()
    }
}