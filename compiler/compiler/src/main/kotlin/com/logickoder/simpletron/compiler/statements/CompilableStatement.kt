package com.logickoder.simpletron.compiler.statements

import com.logickoder.simpletron.compiler.MachineCode

/**
 * Tells that a class can be compiled to machine code
 */
interface CompilableStatement {
    /**
     * Compiles this statement to machine code which may span lines
     *
     * @param config the configuration being used to compile this statement
     */
    fun compile(config: CompilerConfig): MachineCode
}