package com.logickoder.simpletron.compiler.translator.utils

import com.logickoder.simpletron.compiler.translator.Translator
import com.logickoder.simpletron.compiler.translator.syntax.SyntaxResolve
import com.logickoder.simpletron.compiler.translator.syntax.keyword.Keyword
import org.reflections.Reflections
import java.lang.reflect.InvocationTargetException
import java.util.regex.Pattern

/**
 * Returns all the keywords in the given packages
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

fun List<Class<out Keyword>>.findKeyword(keyword: String): Class<out Keyword>? {
    return find { it.simpleName.lowercase() == keyword.lowercase() }
}

fun Translator.findKeyword(keyword: String) = keywords.findKeyword(keyword)

fun Translator.extractKeyword(line: String, index: Int): SyntaxResolve {
    // parse the line to make sure it looks like a correct statement
    val matcher = Pattern.compile("(\\d+)\\s+([a-zA-Z]+)(\\s+[!-~\\s+]+)?").matcher(line)

    return matcher.run {
        if (matches()) {
            // check if the keyword exists, if it doesn't throw a syntax error
            val keyword = findKeyword(group(2)) ?: return SyntaxResolve(
                null,
                "\"${group(2)}\" is not a valid keyword".syntaxError(index)
            )
            try {
                // create an instance of the keyword with the default arguments
                val keywordInstance = keyword
                    .getDeclaredConstructor(Int::class.java, String::class.java)
                    .newInstance(group(1).toInt(), group(3)?.trim() ?: "")
                // return the keyword
                SyntaxResolve(keywordInstance, null)
            } catch (exception: InvocationTargetException) {
                SyntaxResolve(
                    null,
                    exception.cause!!.message!!.syntaxError(index)
                )
            }
        } else {
            // throw a syntax error if the line wasn't constructed correctly
            SyntaxResolve(
                null,
                "unable to parse \"$line\"".syntaxError(index)
            )
        }
    }
}

fun Translator.extractKeyword(index: Int) = extractKeyword(lines[index], index)