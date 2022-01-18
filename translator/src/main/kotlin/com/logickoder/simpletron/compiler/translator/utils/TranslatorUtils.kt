package com.logickoder.simpletron.compiler.translator.utils

import com.logickoder.simpletron.compiler.translator.Translator

fun Translator.extractStatement(index: Int) = extractStatement(lines[index], index)