package dev.logickoder.simpletron.translator.compiler.statements

import dev.logickoder.simpletron.core.common.classInstances
import dev.logickoder.simpletron.core.common.classes
import dev.logickoder.simpletron.translator.statement.Statement
import dev.logickoder.simpletron.translator.statement.StatementFactory

private typealias Factory = StatementFactory<Statement>

private inline fun <reified T : Statement> name(): String {
    return T::class.simpleName?.lowercase()?.run {
        substring(0, indexOf("impl"))
    } ?: ""
}

internal object RemFactoryImpl : Factory(name<RemImpl>()) {
    override fun create(lineNumber: Int, line: String) = RemImpl(lineNumber, line)
}

internal object InputFactoryImpl : Factory(name<InputImpl>()) {
    override fun create(lineNumber: Int, line: String) = InputImpl(lineNumber, line)
}

internal object PrintFactoryImpl : Factory(name<PrintImpl>()) {
    override fun create(lineNumber: Int, line: String) = PrintImpl(lineNumber, line)
}

internal object LetFactoryImpl : Factory(name<LetImpl>()) {
    override fun create(lineNumber: Int, line: String) = LetImpl(lineNumber, line)
}

internal object IfFactoryImpl : Factory(name<IfImpl>()) {
    override fun create(lineNumber: Int, line: String) = IfImpl(lineNumber, line)
}

internal object GotoFactoryImpl : Factory(name<GotoImpl>()) {
    override fun create(lineNumber: Int, line: String) = GotoImpl(lineNumber, line)
}

internal object GosubFactoryImpl : Factory(name<GosubImpl>()) {
    override fun create(lineNumber: Int, line: String) = GosubImpl(lineNumber, line)
}

internal object ReturnFactoryImpl : Factory(name<ReturnImpl>()) {
    override fun create(lineNumber: Int, line: String) = ReturnImpl(lineNumber)
}

internal object EndFactoryImpl : Factory(name<EndImpl>()) {
    override fun create(lineNumber: Int, line: String) = EndImpl(lineNumber)
}

/**
 * Returns the factories of the default statements
 */
fun statementFactories() = classes<Factory>(setOf(RemFactoryImpl::class.java)).classInstances()