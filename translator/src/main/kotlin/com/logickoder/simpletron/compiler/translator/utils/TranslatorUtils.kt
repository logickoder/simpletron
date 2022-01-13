package com.logickoder.simpletron.compiler.translator.utils

import com.logickoder.simpletron.compiler.translator.Translator
import com.logickoder.simpletron.compiler.translator.syntax.Statement
import com.logickoder.simpletron.compiler.translator.syntax.SyntaxElement
import com.logickoder.simpletron.compiler.translator.syntax.SyntaxError
import org.reflections.Reflections
import java.lang.reflect.InvocationTargetException
import java.util.regex.Pattern

/**
 * Returns all the statements in the given packages
 *
 * @param classes set of classes to search for the given type in their package
 * */
inline fun <reified T> classes(classes: Set<Class<out T>>): List<Class<out T>> {
    // retrieve all the required classes defined in the packages of the param [classes]
    return classes.asSequence().map { clazz ->
        Reflections(clazz.packageName).getSubTypesOf(T::class.java)
        // merge all into one set
    }.reduce { acc, otherClasses -> acc.union(otherClasses) }.toList()
}

fun List<Class<out Statement>>.findStatement(keyword: String): Class<out Statement>? {
    return find { it.simpleName.lowercase() == keyword.lowercase() }
}

fun Translator.findStatement(keyword: String) = statements.findStatement(keyword)

fun Translator.extractStatement(line: String, index: Int): SyntaxElement {
    // parse the line to make sure it looks like a correct statement
    val matcher = Pattern.compile("(\\d+)\\s+([a-zA-Z]+)(\\s+[!-~\\s+]+)?").matcher(line)

    return matcher.run {
        if (matches()) {
            // check if the statement exists, if it doesn't throw a syntax error
            val statement = findStatement(group(2))
                ?: return "\"${group(2)}\" is not a valid keyword".syntaxError(index)

            try {
                // create an instance of the statement with the default arguments
                statement
                    .getDeclaredConstructor(Int::class.java, String::class.java)
                    .newInstance(group(1).toInt(), group(3)?.trim() ?: "")
            } catch (exception: InvocationTargetException) {
                val cause = exception.cause ?: exception.targetException
                (cause?.message ?: SyntaxError.UNKNOWN_ERROR).syntaxError(index)
            }
        } else {
            // throw a syntax error if the line wasn't constructed correctly
            "unable to parse \"$line\"".syntaxError(index)
        }
    }
}

fun Translator.extractStatement(index: Int) = extractStatement(lines[index], index)