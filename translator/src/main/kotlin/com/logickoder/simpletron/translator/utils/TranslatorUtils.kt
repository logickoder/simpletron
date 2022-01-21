package com.logickoder.simpletron.translator.utils

import com.logickoder.simpletron.translator.Translator

fun Translator.extractStatement(index: Int) = extractStatement(lines[index], index)