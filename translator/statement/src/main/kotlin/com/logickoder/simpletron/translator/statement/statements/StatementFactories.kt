package com.logickoder.simpletron.translator.statement.statements

import com.logickoder.simpletron.core.utils.classes

object RemFactory : StatementFactory() {
    override fun create(line: String) = Rem(line)
}

object InputFactory : StatementFactory() {
    override fun create(line: String) = Input(line)
}

object PrintFactory : StatementFactory() {
    override fun create(line: String) = Print(line)
}

object LetFactory : StatementFactory() {
    override fun create(line: String) = Let(line)
}

object IfFactory : StatementFactory() {
    override fun create(line: String) = If(line)
}

object GotoFactory : StatementFactory() {
    override fun create(line: String) = Goto(line)
}

object EndFactory : StatementFactory() {
    override fun create(line: String) = End()
}

fun statementFactories(): List<StatementFactory> {
    return classes<StatementFactory>(setOf(RemFactory::class.java)).map { clazz ->
        clazz.kotlin.objectInstance ?: clazz.getDeclaredConstructor().newInstance()
    }
}