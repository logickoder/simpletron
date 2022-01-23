package com.logickoder.simpletron.compiler.statements

import com.logickoder.simpletron.core.utils.classes
import com.logickoder.simpletron.translator.statement.Statement
import com.logickoder.simpletron.translator.statement.statements.StatementFactory

private typealias Factory = StatementFactory<Statement>

private inline fun <reified T : Statement> statementName(): String {
    return T::class.simpleName?.lowercase()?.run {
        substring(0, indexOf("impl"))
    } ?: ""
}

object RemFactoryImpl : Factory(statementName<RemImpl>()) {
    override fun create(lineNumber: Int, line: String) = RemImpl(lineNumber, line)
}

object InputFactoryImpl : Factory(statementName<InputImpl>()) {
    override fun create(lineNumber: Int, line: String) = InputImpl(lineNumber, line)
}

object PrintFactoryImpl : Factory(statementName<PrintImpl>()) {
    override fun create(lineNumber: Int, line: String) = PrintImpl(lineNumber, line)
}

object LetFactoryImpl : Factory(statementName<LetImpl>()) {
    override fun create(lineNumber: Int, line: String) = LetImpl(lineNumber, line)
}

object IfFactoryImpl : Factory(statementName<IfImpl>()) {
    override fun create(lineNumber: Int, line: String) = IfImpl(lineNumber, line)
}

object GotoFactoryImpl : Factory(statementName<GotoImpl>()) {
    override fun create(lineNumber: Int, line: String) = GotoImpl(lineNumber, line)
}

object EndFactoryImpl : Factory(statementName<EndImpl>()) {
    override fun create(lineNumber: Int, line: String) = EndImpl(lineNumber)
}

/**
 * Returns the factories of the default statements
 */
fun statementFactories() = classes<Factory>(setOf(RemFactoryImpl::class.java)).map {
    (it.kotlin.objectInstance ?: it.getDeclaredConstructor().newInstance()) as Factory
}