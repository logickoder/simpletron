package com.logickoder.simpletron.compiler.translator.syntax.statements

import com.logickoder.simpletron.compiler.translator.utils.classes

object RemFactory : StatementFactory() {
    override fun create(lineNumber: Int, line: String) = Rem(lineNumber, line)
}

object InputFactory : StatementFactory() {
    override fun create(lineNumber: Int, line: String) = Input(lineNumber, line)
}

object PrintFactory : StatementFactory() {
    override fun create(lineNumber: Int, line: String) = Print(lineNumber, line)
}

object LetFactory : StatementFactory() {
    override fun create(lineNumber: Int, line: String) = Let(lineNumber, line)
}

object IfFactory : StatementFactory() {
    override fun create(lineNumber: Int, line: String) = If(lineNumber, line)
}

object GotoFactory : StatementFactory() {
    override fun create(lineNumber: Int, line: String) = Goto(lineNumber, line)
}

object EndFactory : StatementFactory() {
    override fun create(lineNumber: Int, line: String) = End(lineNumber)
}

fun statementFactories(): List<StatementFactory> {
    return classes<StatementFactory>(setOf(RemFactory::class.java)).map { clazz ->
        clazz.kotlin.objectInstance ?: clazz.getDeclaredConstructor().newInstance()
    }
}