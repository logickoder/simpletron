package com.logickoder.simpletron.translator.statement.statements

abstract class RemFactory : StatementFactory<Rem>(Rem::class.java)

abstract class InputFactory : StatementFactory<Input>(Input::class.java)

abstract class PrintFactory : StatementFactory<Print>(Print::class.java)

abstract class LetFactory : StatementFactory<Let>(Let::class.java)

abstract class IfFactory : StatementFactory<If>(If::class.java)

abstract class GotoFactory : StatementFactory<Goto>(Goto::class.java)

abstract class EndFactory : StatementFactory<End>(End::class.java)