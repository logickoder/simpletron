package com.logickoder.simpletron.core.utils

import org.reflections.Reflections

typealias Classes<T> = List<Class<out T>>

/**
 * Returns the classes that are subtypes of the generic type in the given packages
 *
 * @param packages set of classes to search for the given type in their package
 * */
inline fun <reified T> classes(packages: Set<Class<*>>): Classes<T> {
    // retrieve all the required classes defined in the packages of the param [classes]
    return packages.asSequence().map { clazz ->
        Reflections(clazz.packageName).getSubTypesOf(T::class.java)
        // merge all into one set
    }.reduce { acc, otherClasses -> acc.union(otherClasses) }.toList()
}

/**
 * Creates instances of classes, only if they are classes without constructors
 */
fun <T> Classes<T>.classInstances(): List<T> = map {
    (it.javaClass.kotlin.objectInstance ?: it.getDeclaredConstructor().newInstance()) as T
}