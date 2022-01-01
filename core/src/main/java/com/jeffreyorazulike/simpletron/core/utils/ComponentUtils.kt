package com.jeffreyorazulike.simpletron.core.utils

import org.reflections.Reflections

/**
 * Returns the instances of the specified types in the package of the class set of classes,
 * make sure the classes being instantiated have a no-args constructor
 *
 * @param classes set of classes to search for the given type in their package
 * */
inline fun <reified T> classInstances(classes: Set<Class<*>>): List<T> {
    // retrieve all the required classes defined in the packages of the param [classes]
    return classes.asSequence().map { clazz ->
        Reflections(clazz.packageName).getSubTypesOf(T::class.java)
    }.reduce { acc, otherClasses ->
        // merge all into one set
        acc.union(otherClasses)
    }.map {
        // return instances of each of them
        it.kotlin.objectInstance ?: it.getDeclaredConstructor().newInstance()
    }
}