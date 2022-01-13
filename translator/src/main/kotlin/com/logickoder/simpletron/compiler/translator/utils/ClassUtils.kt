package com.logickoder.simpletron.compiler.translator.utils

import org.reflections.Reflections

/**
 * Returns all the classes in the given packages
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